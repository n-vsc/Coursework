package dsx.bcv.server.controllers;

import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.security.JwtTokenProvider;
import dsx.bcv.server.services.api_connectors.ApiConnectorName;
import dsx.bcv.server.services.csv_parsers.data_formats.CsvFileFormat;
import dsx.bcv.server.services.data_services.PortfolioService;
import dsx.bcv.server.services.data_services.UserService;
import dsx.bcv.server.views.TransactionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Предоставляет api для CRUD операций с транзакциями пользователей
 */
@RestController
@RequestMapping("transactions")
@Slf4j
public class TransactionController {

    private final UserService userService;
    private final PortfolioService portfolioService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ConversionService conversionService;

    public TransactionController(
            UserService userService,
            PortfolioService portfolioService,
            JwtTokenProvider jwtTokenProvider,
            ConversionService conversionService
    ) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.conversionService = conversionService;
    }

    @GetMapping
    public List<TransactionVO> getTransactions(
            @RequestParam long portfolioId,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        var transactions = portfolioService.getTransactions(portfolioId);
        return transactions.stream()
                .map(transaction -> conversionService.convert(transaction, TransactionVO.class))
                .collect(Collectors.toList());
    }

    @PostMapping("uploadFile")
    public void uploadFile(
            @RequestParam long portfolioId,
            @RequestParam("file") MultipartFile file,
            @RequestParam CsvFileFormat csvFileFormat,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        portfolioService.uploadTransactionFile(file, csvFileFormat, portfolioId);
    }

    @PostMapping("uploadUsingApi")
    public void uploadUsingApi(
            @RequestParam long portfolioId,
            @RequestParam("connector") ApiConnectorName apiConnectorName,
            @RequestParam("token") String token,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        portfolioService.uploadTransactionsUsingApi(apiConnectorName, token, portfolioId);
    }

    @GetMapping("{id}")
    public TransactionVO findByID(
            @PathVariable long id,
            @RequestParam long portfolioId,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        var transaction = portfolioService.findTransactionById(id, portfolioId);
        return conversionService.convert(transaction, TransactionVO.class);
    }

    @PostMapping
    public TransactionVO add(
            @RequestBody TransactionVO transactionVO,
            @RequestParam long portfolioId,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        var transaction = conversionService.convert(transactionVO, Transaction.class);
        assert transaction != null;
        return conversionService.convert(
                portfolioService.addTransaction(transaction, portfolioId),
                TransactionVO.class
        );
    }

    @PutMapping("{id}")
    public TransactionVO update(@PathVariable long id, @RequestBody TransactionVO transactionVO) {
        throw new UnsupportedOperationException();
//        log.info(
//                "Request received. Url: {}",
//                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
//        );
//        var transaction = conversionService.convert(transactionVO, Transaction.class);
//        var newTransaction = transactionService.updateById(id, transaction);
//        return conversionService.convert(newTransaction, TransactionVO.class);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable long id,
            @RequestParam long portfolioId,
            @RequestHeader("Authorization") String authorization
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        userService.ThrowNotFoundIfUserDoesntHavePortfolioWithId(
                getUsernameFromToken(removePrefixFromToken(authorization)),
                portfolioId
        );
        portfolioService.deleteTransactionById(id, portfolioId);
    }

    private String removePrefixFromToken(String authorization) {
        return jwtTokenProvider.removePrefixFromToken(authorization);
    }

    private String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsername(token);
    }
}
