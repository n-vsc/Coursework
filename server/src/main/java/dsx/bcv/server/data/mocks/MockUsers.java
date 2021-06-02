package dsx.bcv.server.data.mocks;

import dsx.bcv.server.data.models.Role;
import dsx.bcv.server.data.models.User;
import dsx.bcv.server.services.data_services.PortfolioService;
import dsx.bcv.server.services.data_services.RoleService;
import dsx.bcv.server.services.data_services.UserService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Добавляет mock пользователей для ручного тестирования
 */
@Component
@DependsOn({"mockPortfolios"})
public class MockUsers {

    private MockUsers(
            UserService userService,
            RoleService roleService,
            PortfolioService portfolioService
    ) {
        var userRole = new Role("USER");
        roleService.save(userRole);

        var user1 = new User(
                "username",
                "password",
                "email@gmail.com");

        var user2 = new User(
                "username2",
                "password2",
                "email@gmail.com");

        var portfolio = portfolioService.findByName("my_portfolio");
        var portfolio2 = portfolioService.findByName("my_portfolio2");

        if (userService.count() == 0) {
            userService.register(user1);
            userService.addPortfolioByUsername(user1.getUsername(), portfolio);
            userService.register(user2);
            userService.addPortfolioByUsername(user2.getUsername(), portfolio2);
        }
    }
}
