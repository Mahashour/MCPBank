package com.example.banksys.shared.ai;

import com.example.banksys.account.application.AccountService;
import com.example.banksys.account.interfaces.dto.AccountCreateRequest;
import com.example.banksys.account.interfaces.dto.AccountResponse;
import com.example.banksys.client.application.ClientService;
import com.example.banksys.client.domain.model.ClientStatus;
import com.example.banksys.client.domain.model.NationalId;
import com.example.banksys.client.domain.model.PhoneNumber;
import com.example.banksys.client.interfaces.dto.ClientCreateRequest;
import com.example.banksys.client.interfaces.dto.ClientPublicResponse;
import com.example.banksys.transaction.application.TransactionService;
import com.example.banksys.transaction.interfaces.dto.DepositRequest;
import com.example.banksys.transaction.interfaces.dto.TransactionResponse;
import com.example.banksys.transaction.interfaces.dto.TransferRequest;
import com.example.banksys.transaction.interfaces.dto.WithdrawalRequest;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class BankingWriteTools {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public BankingWriteTools(ClientService clientService, AccountService accountService, TransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @McpTool(description = """
             Creates a new bank client.
             Returns the client's masked info if successful.
            """)
    public ClientPublicResponse createClient(
            @McpToolParam(description = "the client's first name") String firstName,
            @McpToolParam(description = "the client's last name") String lastName,
            @McpToolParam(description = "the type of national ID, either \"EG\" or \"FOREIGN\"") String nationalIdType,
            @McpToolParam(description = "A valid value of a national ID.") String nationalIdValue,
            @McpToolParam(description = "The client's email", required = false) String email,
            @McpToolParam(description = "Set of phone numbers in E.164 format separated by commas with no spaces. Example: \"+201234567890,+201212023332\" or just \"+201234567890\" if one number.", required = false) String phoneNumbers
    ) {
        NationalId nationalId = NationalId.makeNationalId(nationalIdType, nationalIdValue);

        Set<PhoneNumber> phoneNumberSet = new HashSet<>();
        if (phoneNumbers != null && !phoneNumbers.isBlank()) {
            for (String number : phoneNumbers.split(",")) {
                phoneNumberSet.add(new PhoneNumber(number.trim()));
            }
        }

        ClientCreateRequest request = new ClientCreateRequest(firstName, lastName, nationalId, phoneNumberSet, (email != null && !email.isBlank()) ? email : null);

        return clientService.createClient(request);
    }

    @McpTool(description = """
             Create a new bank account for an existing client.
             A client must already exist.
             Returns the account info if successful.
            """)
    public AccountResponse createAccount(
            @McpToolParam(description = "The UUID of the client") String clientId,
            @McpToolParam(description = "The initial amount in the account. Default is 0.", required = false) String initialBalance
    ) {
        BigDecimal initBal;
        if (initialBalance == null || initialBalance.isBlank()) {
            initBal = BigDecimal.ZERO;
        } else {
            initBal = new BigDecimal(initialBalance);
        }

        AccountCreateRequest request = new AccountCreateRequest(UUID.fromString(clientId), initBal);
        return accountService.createAccount(request);
    }

    @McpTool(description = """
             Transfers money from one account to another.
             The accounts must already exist.
             The account being transferred from must have enough money for the transfer amount.
             Returns the transaction info if successful.
            """)
    public TransactionResponse transferMoney(
            @McpToolParam(description = "The account being transferred from. Must have enough money.") String fromAccountId,
            @McpToolParam(description = "The account being transferred to.") String toAccountId,
            @McpToolParam(description = "The amount of money. Must be a positive number.") String amount,
            @McpToolParam(description = "The description of the transaction.", required = false) String description) {

        TransferRequest req = new TransferRequest(Long.valueOf(fromAccountId), Long.valueOf(toAccountId), new BigDecimal(amount), description);
        return transactionService.transfer(req);
    }

    @McpTool(description = """
             Deposits money into an account.
             The account must already exist.
             Returns the transaction info if successful.
            """)
    public TransactionResponse depositMoney(
            @McpToolParam(description = "The account being transferred to.") String toAccountId,
            @McpToolParam(description = "The amount of money. Must be positive.") String amount,
            @McpToolParam(description = "The description of the transaction.", required = false) String description) {

        DepositRequest req = new DepositRequest(Long.valueOf(toAccountId), new BigDecimal(amount), description);
        return transactionService.deposit(req);
    }

    @McpTool(description = """
             Withdraws money from an account.
             The account must already exist.
             The account must have enough money for the withdrawal amount.
             Returns the transaction info if successful.
            """)
    public TransactionResponse withdrawMoney(
            @McpToolParam(description = "The account being transferred from. Must have enough money.") String fromAccountId,
            @McpToolParam(description = "The amount of money. Must be positive.") String amount,
            @McpToolParam(description = "The description of the transaction.", required = false) String description) {

        WithdrawalRequest req = new WithdrawalRequest(Long.valueOf(fromAccountId), new BigDecimal(amount), description);
        return transactionService.withdraw(req);
    }

    @McpTool(description = """
             Used to update a client's first name, last name, or email.
             Must input the client's UUID and the new values.
             Leave any values you don't wish to update null.
             Returns the client's updated info if successful.
            """)
    public ClientPublicResponse updateClient(
            @McpToolParam(description = "The client's UUID") String clientId,
            @McpToolParam(description = "The client's first name", required = false) String firstName,
            @McpToolParam(description = "The client's last name.", required = false) String lastName,
            @McpToolParam(description = "The client's email", required = false) String email) {
        return clientService.updateClient(UUID.fromString(clientId), firstName, lastName, email);
    }

    @McpTool(description = """
             Used to change a client's status.
             Must input client UUID and status.
            """)
    public ClientPublicResponse changeClientStatus(
            @McpToolParam(description = "The client's UUID") String clientId,
            @McpToolParam(description = "The client's first name") String status) {
        return clientService.changeClientStatus(UUID.fromString(clientId), ClientStatus.valueOf(status.toUpperCase()));
    }

    @McpTool(description = """
             Used to add a new phone number to the client.
            """)
    public ClientPublicResponse addClientPhone(
            @McpToolParam(description = "The client's UUID") String clientId,
            @McpToolParam(description = "The new phone number to be added. Must be in a E.164 format, no spaces") String phoneNumber) {
        return clientService.addClientPhone(UUID.fromString(clientId), new PhoneNumber(phoneNumber));
    }

    @McpTool(description = """
             Used to remove a phone number from the client.
            """)
    public ClientPublicResponse removeClientPhone(
            @McpToolParam(description = "The client's UUID") String clientId,
            @McpToolParam(description = "The phone number to be removed. Must be in a E.164 format, no spaces") String phoneNumber) {
        return clientService.removeClientPhone(UUID.fromString(clientId), new PhoneNumber(phoneNumber));
    }
}
