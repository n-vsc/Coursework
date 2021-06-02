package dsx.bcv.server.services;

import dsx.bcv.server.Application;
import dsx.bcv.server.data.models.*;
import dsx.bcv.server.services.csv_parsers.CsvParser;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CsvParserTest {

    @Autowired
    private CsvParser csvParser;

    @Test
    public void parseTrades() throws IOException {

        var trades = "2019-11-25 8:08:44;eosbtc;Buy;1;EOS;0,0003597;BTC;0,0003597;BTC;0,0025;EOS;Invest;37605094;877454884\n" +
                "2019-11-08 21:34:39;ethusd;Sell;0,01645307;ETH;186,3625;USD;3,06624;USD;0,01;USD;Trade;37550914;853672518\n";

        var result = csvParser.parseTrades(new StringReader(trades), ';');

        var expected = Arrays.asList(
                new Trade(LocalDateTime.parse("2019-11-25T08:08:44"),
                        new Instrument(new Currency("eos"), new Currency("btc")),
                        TradeType.Buy,
                        new BigDecimal("1"),
                        new Currency("eos"),
                        new BigDecimal("0.0003597"),
                        new Currency("btc"),
                        new BigDecimal("0.0025"),
                        new Currency("eos"),
                        "37605094"),
                new Trade(LocalDateTime.parse("2019-11-08T21:34:39"),
                        new Instrument(new Currency("eth"), new Currency("usd")),
                        TradeType.Sell,
                        new BigDecimal("0.01645307"),
                        new Currency("eth"),
                        new BigDecimal("186.3625"),
                        new Currency("usd"),
                        new BigDecimal("0.01"),
                        new Currency("usd"),
                        "37550914")
        );

        for (var i = 0; i < expected.size(); i++){
            expected.get(i).setId(result.get(i).getId());
        }

        assertEquals(expected, result);
    }

    @Test
    public void parseTransactions() throws IOException {

        var transactions = "2019-11-27 15:29:05;Deposit;BTC;0,00028643;0;SYSTEM exchange;Invest;Complete;;3765477;;;\n" +
                "2019-11-27 15:29:05;Withdraw;EOS;0,7975;0;Invest;ExchangeAccount;Complete;;3765476;;;\n";

        var result = csvParser.parseTransactions(new StringReader(transactions), ';');

        var expected = Arrays.asList(
                new Transaction(LocalDateTime.parse("2019-11-27T15:29:05"),
                        TransactionType.Deposit,
                        new Currency("btc"),
                        new BigDecimal("0.00028643"),
                        new BigDecimal("0"),
                        TransactionStatus.Complete,
                        "3765477"),
                new Transaction(LocalDateTime.parse("2019-11-27T15:29:05"),
                        TransactionType.Withdraw,
                        new Currency("eos"),
                        new BigDecimal("0.7975"),
                        new BigDecimal("0"),
                        TransactionStatus.Complete,
                        "3765476")
        );

        for (var i = 0; i < expected.size(); i++){
            expected.get(i).setId(result.get(i).getId());
        }

        assertEquals(expected, result);
    }
}