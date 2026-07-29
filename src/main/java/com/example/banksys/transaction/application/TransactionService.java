package com.example.banksys.transaction.application;

import com.example.banksys.account.application.AccountService;
import com.example.banksys.account.domain.model.Account;
import com.example.banksys.transaction.domain.model.Transaction;
import com.example.banksys.transaction.domain.model.TransactionType;
import com.example.banksys.transaction.interfaces.dto.DepositRequest;
import com.example.banksys.transaction.interfaces.dto.TransactionResponse;
import com.example.banksys.transaction.interfaces.dto.TransferRequest;
import com.example.banksys.transaction.interfaces.dto.WithdrawalRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public TransactionResponse transfer(TransferRequest request) {
        Account from = accountService.getAccountEntity(request.fromAccountId());
        Account to = accountService.getAccountEntity(request.toAccountId());

        if (from.getBalance().compareTo(request.amount()) < 0)
            throw new RuntimeException("Insufficient funds");

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));
        accountService.saveAccount(from);
        accountService.saveAccount(to);

        Transaction tx = new Transaction(from, to, request.amount(), TransactionType.TRANSFER, request.description());
        tx = transactionRepository.save(tx);
        return toResponse(tx);
    }

    public TransactionResponse deposit(DepositRequest request) {
        Account to = accountService.getAccountEntity(request.toAccountId());
        to.setBalance(to.getBalance().add(request.amount()));
        accountService.saveAccount(to);

        Transaction tx = new Transaction(null, to, request.amount(), TransactionType.DEPOSIT, request.description());
        tx = transactionRepository.save(tx);
        return toResponse(tx);
    }

    public TransactionResponse withdraw(WithdrawalRequest request) {
        Account from = accountService.getAccountEntity(request.fromAccountId());
        if (from.getBalance().compareTo(request.amount()) < 0)
            throw new RuntimeException("Insufficient funds");
        from.setBalance(from.getBalance().subtract(request.amount()));
        accountService.saveAccount(from);

        Transaction tx = new Transaction(from, null, request.amount(), TransactionType.WITHDRAWAL, request.description());
        tx = transactionRepository.save(tx);
        return toResponse(tx);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(Long accountId) {
        return transactionRepository.findByFromAccountAccountIdOrToAccountAccountIdOrderByTimestampDesc(accountId, accountId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(UUID accountId) {
        return toResponse(transactionRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Transaction not found")));
    }

    private TransactionResponse toResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(), tx.getType(), tx.getAmount(),
                tx.getTimestamp(), tx.getDescription(),
                tx.getFromAccount() != null ? tx.getFromAccount().getAccountId() : null,
                tx.getToAccount() != null ? tx.getToAccount().getAccountId() : null
        );
    }
}
