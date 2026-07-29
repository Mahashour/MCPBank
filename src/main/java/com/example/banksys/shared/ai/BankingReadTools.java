package com.example.banksys.shared.ai;

import com.example.banksys.account.application.AccountService;
import com.example.banksys.account.interfaces.dto.AccountResponse;
import com.example.banksys.client.application.ClientService;
import com.example.banksys.client.interfaces.dto.ClientPublicResponse;
import com.example.banksys.transaction.application.TransactionService;
import com.example.banksys.transaction.domain.model.Transaction;
import com.example.banksys.transaction.interfaces.dto.TransactionResponse;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BankingReadTools {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public BankingReadTools(ClientService clientService, AccountService accountService, TransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @McpTool(description = """
            Get a client's public information by bank ID (UUID).
            Returns name, masked national ID, email, and phone numbers""")
    public ClientPublicResponse getClientById(
            @McpToolParam(description = "The client's UUID, also known as the bank id.") String bankId) {
        return clientService.getClientPublic(UUID.fromString(bankId));
    }

    @McpTool(description = """
            Returns a list of all clients and their information (masked ID)""")
    public List<ClientPublicResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @McpTool(description = """
            Search clients by first name and/or last name. 
            Returns a list of public client info.
            A parameter may be set to null or blank to only search by the other one.
            For example, if you want to search by first name only, leave lastName as null.
            You may search with both for a more refined search.
            The names are an exact match.""")
    public List<ClientPublicResponse> searchClient(
            @McpToolParam(description = "The first name of the client", required = false) String firstName,
            @McpToolParam(description = "The last name of the client", required = false) String lastName) {
        return clientService.searchClients(firstName, lastName);
    }

    @McpTool(description = """
            Returns a list of all accounts with their information"""
    )
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @McpTool(description = """
            Gets all the client's bank accounts by the client's UUID"""
    )
    public List<AccountResponse> getAccountsByClient(
            @McpToolParam(description = "The client's UUID") String clientId) {
        return accountService.getAccountsByClient(UUID.fromString(clientId));
    }

    @McpTool(description = """
            Gets a single account by the account's ID"""
    )
    public AccountResponse getAccount(
            @McpToolParam(description = "The account's ID") String accountId) {
        return accountService.getAccount(Long.valueOf(accountId));
    }

    @McpTool(description = """
            Gets all the account's transactions using the account's id""")
    public List<TransactionResponse> getAccountTransactions(
            @McpToolParam(description = "The account's ID.") String accountId) {
        return transactionService.getAccountTransactions(Long.valueOf(accountId));
    }

    @McpTool(description = """
            Gets all the transactions""")
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @McpTool(description = """
            Gets a specific transaction by its UUID""")
    public TransactionResponse getTransactionById(
            @McpToolParam(description = "The transaction's UUID") String transactionId){
        return transactionService.getTransactionById(UUID.fromString(transactionId));
    }
}
