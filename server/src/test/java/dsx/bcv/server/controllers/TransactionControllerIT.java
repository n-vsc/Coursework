package dsx.bcv.server.controllers;

import dsx.bcv.server.Application;
import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.data.models.TransactionStatus;
import dsx.bcv.server.data.models.TransactionType;
import dsx.bcv.server.data.repositories.TransactionRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import dsx.bcv.server.services.ToJsonConverterService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ToJsonConverterService toJsonConverterService;

    private final String controllerUrl = "/transactions/";

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    public TransactionControllerIT() {

        transaction1 = new Transaction(
                LocalDateTime.now(),
                TransactionType.Deposit,
                new Currency("BTC"),
                new BigDecimal("0.21043"),
                new BigDecimal("0"),
                TransactionStatus.Complete,
                "3669993");

        transaction2 = new Transaction(
                LocalDateTime.now(),
                TransactionType.Withdraw,
                new Currency("EUR"),
                new BigDecimal("0.21043"),
                new BigDecimal("0"),
                TransactionStatus.Complete,
                "3621995");

        transaction3 = new Transaction(
                LocalDateTime.now(),
                TransactionType.Deposit,
                new Currency("RUB"),
                new BigDecimal("0.21043"),
                new BigDecimal("0"),
                TransactionStatus.Complete,
                "1528613");
    }

    @Before
    public void setUp() throws Exception {

        mockMvc.perform(post(controllerUrl)
                .content(toJsonConverterService.toJson(transaction1))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        mockMvc.perform(post(controllerUrl)
                .content(toJsonConverterService.toJson(transaction2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        );
    }

    @Test
    public void getAll() throws Exception {

        mockMvc.perform(get(controllerUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].transactionType").isNotEmpty())    //?
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getByID() throws Exception {

        final var id = getAnyId();

        mockMvc.perform(get(controllerUrl + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.transactionType").isNotEmpty())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void add() throws Exception {

        mockMvc.perform(post(controllerUrl)
                .content(toJsonConverterService.toJson(transaction3))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.transactionType").value(transaction3.getTransactionType().toString()));
    }

    @Test
    public void update() throws Exception {

        final var id = getAnyId();

        mockMvc.perform(
                put(controllerUrl + id)
                        .content(toJsonConverterService.toJson(transaction1))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.transactionType").value(transaction1.getTransactionType().toString()))
                .andReturn();
    }

    @Test
    public void delete() throws Exception {

        final var id = getAnyId();

        mockMvc.perform(MockMvcRequestBuilders.delete(controllerUrl + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.delete(controllerUrl + id))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    private long getAnyId() {
        return StreamSupport.stream(transactionRepository.findAll().spliterator(), false)
                .map(Transaction::getId)
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}