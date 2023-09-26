package com.example.service;

import com.example.entity.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAllAccounts();

    Account getAccountById(Integer id);

    Account saveAccount(Account account);

    Account updateAccount(Integer id, Account account);

    void deleteAccount(Integer id);

    Account getAccountByUsername(String username);

    Account authenticate(String username, String password);

}