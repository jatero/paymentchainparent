package com.paymentchain.transactions.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.repository.TransactionRespository;


@RestController
@RequestMapping("/transaction")
public class TransactionRestController {
	
	@Autowired
	TransactionRespository transactionRepository;
	
	@GetMapping()
	public ResponseEntity<List<Transaction>> list(){
		List<Transaction> transactions = transactionRepository.findAll();
		if(transactions != null && !transactions.isEmpty()) {
			return ResponseEntity.ok(transactions);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Transaction> get(@PathVariable Long id) {
		return transactionRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Transaction input) {       
        Transaction save = transactionRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/transactions")
    public List<Transaction> accounts(@RequestParam String ibanAccount){
    	return transactionRepository.findAllByIbanAccount(ibanAccount);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
 	   Optional<Transaction> deleted = Optional.ofNullable(transactionRepository.findById(id).get());
 	   if (deleted.isPresent()) {
 		   transactionRepository.deleteById(id);
		   return ResponseEntity.ok(deleted);
 	   } else {
 		   return ResponseEntity.notFound().build();   
 	   }
    }
 	      
}
