package gr.aueb.cf.bankapp.service;

import gr.aueb.cf.bankapp.core.exception.AccountNotFoundException;
import gr.aueb.cf.bankapp.core.exception.InsufficientAmountException;
import gr.aueb.cf.bankapp.core.exception.NegativeAmountException;
import gr.aueb.cf.bankapp.core.mapper.Mapper;
import gr.aueb.cf.bankapp.dao.IAccountDAO;
import gr.aueb.cf.bankapp.dto.AccountInsertDTO;
import gr.aueb.cf.bankapp.dto.AccountReadOnlyDTO;
import gr.aueb.cf.bankapp.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements IAccountService {
    private final IAccountDAO accountDAO;

    public AccountServiceImpl(IAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public boolean createNewAccount(AccountInsertDTO dto) {
        Account account = Mapper.mapToModelEntity(dto);
        accountDAO.saveOrUpdate(account);  // storage
        return true;
    }

    @Override
    public void deposit(String iban, BigDecimal amount) throws NegativeAmountException, AccountNotFoundException {
        try {
            Account account = accountDAO.getByIban(iban)
                    .orElseThrow(() -> new AccountNotFoundException("Account with iban " + iban + " not found"));

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativeAmountException("Invalid amount " + amount + ". Amount must be positive (input waw negative)");
            }

            account.setBalance(account.getBalance().add(amount));
            accountDAO.saveOrUpdate(account);       //  storage
        } catch (NegativeAmountException e) {
            System.err.printf("%s. The amount=%f is negative. \n%s", LocalDateTime.now(), amount, e);
            throw e;
        } catch (AccountNotFoundException e) {
            System.err.printf("%s. The account with iban=%s not found \n%s", LocalDateTime.now(), iban, e);
            throw e;
        }
    }

    @Override
    public void withdraw(String iban, BigDecimal amount) throws NegativeAmountException, AccountNotFoundException, InsufficientAmountException {
        try {
            Account account = accountDAO.getByIban(iban)
                    .orElseThrow(() -> new AccountNotFoundException("Account with iban " + iban + " not found"));

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativeAmountException("Invalid amount " + amount + ". Amount must be positive (input waw negative)");
            }

            if (amount.compareTo(account.getBalance()) > 0) {
                throw new InsufficientAmountException("Invalid amount " + amount + ". Amount must be less or equal of balance (input was greater)");
            }

            account.setBalance(account.getBalance().subtract(amount));
            accountDAO.saveOrUpdate(account);       //  storage
        } catch (NegativeAmountException e) {
            System.err.printf("%s. The amount=%f is negative. \n%s", LocalDateTime.now(), amount, e);
            throw e;
        } catch (AccountNotFoundException e) {
            System.err.printf("%s. The account with iban=%s not found \n%s", LocalDateTime.now(), iban, e);
            throw e;
        } catch (InsufficientAmountException e) {
            System.err.printf("\"%s. The amount=%f is greater than balance \n%s", LocalDateTime.now(), amount, e);
        }
    }


    @Override
    public BigDecimal getBalance(String iban) throws AccountNotFoundException {
        try {
            Account account = accountDAO.getByIban(iban)
                    .orElseThrow(() -> new AccountNotFoundException("Account with iban " + iban + " not found"));
            return account.getBalance();

        } catch (AccountNotFoundException e) {
            System.err.printf("%s. The account with iban=%s not found \n%s", LocalDateTime.now(), iban, e);
            throw e;
        }
    }

    @Override
    public List<AccountReadOnlyDTO> getAccounts() {
//        return accountDAO.getAccounts()
//                .stream()
//                .map(Mapper::mapToReadOnlyDTO)
//                .collect(Collectors.toList());

        List<AccountReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
        List<Account> accounts = accountDAO.getAccounts();

        for (Account account : accounts) {
            readOnlyDTOS.add(Mapper.mapToReadOnlyDTO(account));
        }
        return readOnlyDTOS;
    }
}