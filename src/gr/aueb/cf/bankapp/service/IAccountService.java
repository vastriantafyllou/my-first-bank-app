package gr.aueb.cf.bankapp.service;


import gr.aueb.cf.bankapp.core.exception.AccountNotFoundException;
import gr.aueb.cf.bankapp.core.exception.InsufficientAmountException;
import gr.aueb.cf.bankapp.core.exception.NegativeAmountException;
import gr.aueb.cf.bankapp.dto.AccountInsertDTO;
import gr.aueb.cf.bankapp.dto.AccountReadOnlyDTO;

import java.math.BigDecimal;
import java.util.List;


public interface IAccountService {
    boolean createNewAccount(AccountInsertDTO dto);
    void deposit(String iban, BigDecimal amount) throws NegativeAmountException, AccountNotFoundException;
    void withdraw(String iban, BigDecimal amount) throws NegativeAmountException, AccountNotFoundException, InsufficientAmountException;
    BigDecimal getBalance(String iban) throws AccountNotFoundException;
    List<AccountReadOnlyDTO> getAccounts();


}
