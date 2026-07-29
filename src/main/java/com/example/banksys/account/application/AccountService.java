package com.example.banksys.account.application;

import com.example.banksys.account.domain.model.Account;
import com.example.banksys.account.interfaces.dto.AccountCreateRequest;
import com.example.banksys.account.interfaces.dto.AccountResponse;
import com.example.banksys.client.application.ClientRepository;
import com.example.banksys.client.application.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public AccountService(AccountRepository accountRepository, ClientService clientService){
        this.accountRepository = accountRepository;
        this.clientService = clientService;
    }
    public AccountResponse createAccount(AccountCreateRequest request){
        Account account = new Account(
                clientService.findClientById(request.clientId()),
                request.initialBalance()
        );
        account = accountRepository.save(account);
        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public Account getAccountEntity(Long accountId){
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts(){
        return accountRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByClient(UUID clientId){
        return accountRepository.findByClient_BankId(clientId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    private AccountResponse toResponse(Account account){
        return new AccountResponse(
                account.getAccountId(),
                account.getClient().getBankId(),
                account.getBalance(),
                account.getStatus()
        );
    }
}
