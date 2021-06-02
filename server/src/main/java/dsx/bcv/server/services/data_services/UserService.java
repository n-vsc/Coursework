package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Portfolio;
import dsx.bcv.server.data.models.Role;
import dsx.bcv.server.data.models.User;
import dsx.bcv.server.data.repositories.UserRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Позволяет делать CRUD операции с пользователями и их портфелями
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PortfolioService portfolioService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(
            UserRepository userRepository,
            PortfolioService portfolioService,
            RoleService roleService
    ) {
        this.userRepository = userRepository;
        this.portfolioService = portfolioService;
        this.roleService = roleService;
    }

    public User register(User user) {
        Role userRole = roleService.findByName("USER");
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("register: User {} saved", registeredUser);
        return registeredUser;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        var users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        log.info("findAll: {} users found", users.size());
        return users;
    }

    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        log.info("findByUsername: {} found by username {}", user, username);
        return user;
    }

    public User findById(long id) {
        var user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("findByUsername: {} found by id {}", user, id);
        return user;
    }

    @Transactional
    public void deleteById(long id) {
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.deleteById(id);
        log.info("delete: User with id {} successfully deleted", id);
    }

    public Portfolio addPortfolioByUsername(String username, Portfolio portfolio) {
        var user = findByUsername(username);
        var savedPortfolio = portfolioService.save(portfolio);
        user.getPortfolios().add(savedPortfolio);
        userRepository.save(user);
        log.debug("addPortfolioByUsername: Portfolio {} added to user {}", savedPortfolio, user);
        return savedPortfolio;
    }

    public List<Portfolio> findAllPortfoliosByUsername(String username) {
        var user = findByUsername(username);
        var portfolios = new ArrayList<>(user.getPortfolios());
        log.debug("findAllPortfoliosByUsername: Portfolios {} found by user {}", portfolios, user);
        return portfolios;
    }

    public Portfolio findPortfolioByUsernameAndPortfolioId(String username, long portfolioId) {
        var user = findByUsername(username);
        var portfolio = user.getPortfolios().stream()
                .filter(p -> p.getId() == portfolioId)
                .findFirst()
                .orElseThrow(NotFoundException::new);
        log.debug(
                "findPortfolioByUsernameAndPortfolioId: Portfolio {} found by id {}",
                portfolio,
                portfolioId
        );
        return portfolio;
    }

    public Portfolio findPortfolioByUsernameAndPortfolioName(String username, String portfolioName) {
        var user = findByUsername(username);
        var portfolio = user.getPortfolios().stream()
                .filter(p -> p.getName().equals(portfolioName))
                .findFirst()
                .orElseThrow(NotFoundException::new);
        log.debug(
                "findPortfolioByUsernameAndPortfolioName: Portfolio {} found by name {}",
                portfolio,
                portfolioName
        );
        return portfolio;
    }

    public Portfolio updatePortfolioByUsernameAndPortfolioId(
            String username,
            long portfolioId,
            Portfolio portfolio
    ) {
        log.debug("updatePortfolioByUsernameAndPortfolioId: Called (Not implemented)");
        throw new RuntimeException("Portfolio updating is not implemented");
    }

    public void deletePortfolioByUsernameAndPortfolioId(String username, long portfolioId) {
        var user = findByUsername(username);
        user.getPortfolios().removeIf(portfolio -> portfolio.getId() == portfolioId);
        save(user);
        portfolioService.deleteById(portfolioId);
        log.info("delete: Portfolio with id {} successfully deleted", portfolioId);
    }

    public void ThrowNotFoundIfUserDoesntHavePortfolioWithId(String username, long portfolioId) {
        var user = findByUsername(username);
        log.debug(
                "ThrowNotFoundIfUserDoesntHavePortfolioWithId: Check that user {} has a portfolio with id {}",
                user,
                portfolioId
        );
        user.getPortfolios().stream()
                .filter(portfolio -> portfolio.getId() == portfolioId)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public long count() {
        log.debug("count: Called");
        return userRepository.count();
    }
}
