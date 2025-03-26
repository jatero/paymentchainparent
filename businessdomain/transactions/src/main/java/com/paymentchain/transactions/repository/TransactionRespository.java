package com.paymentchain.transactions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentchain.transactions.entities.Transaction;

public interface TransactionRespository extends JpaRepository<Transaction, Long> {
	
	public List<Transaction> findAllByIbanAccount(String ibanAccount);

}
