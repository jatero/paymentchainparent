package com.paymentchain.transactions.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Transaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull
	private double amount;
	private Date date;
	private String channel;
	private double fee;
	@NotNull
	private String ibanAccount;
	private String reference;
	private String status;	

}
