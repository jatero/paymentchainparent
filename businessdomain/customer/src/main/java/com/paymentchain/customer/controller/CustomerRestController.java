package com.paymentchain.customer.controller;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.exception.BussinesRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import com.paymentchain.customer.services.BussinesTransaction;


@RestController
@RequestMapping("/customer")
public class CustomerRestController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BussinesTransaction bt;
	
	@GetMapping("/full")
	public ResponseEntity<Customer> get(@RequestParam String code) {
		Customer customer = bt.get(code);

		return ResponseEntity.ok(customer);
	}
		
	
	@GetMapping
	public ResponseEntity<List<Customer>> list(){
		List<Customer> customers = customerRepository.findAll();
		if (customers == null || customers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.ok(customers);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getById(@PathVariable long id){
		return customerRepository.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<Customer> post(@RequestBody Customer input) throws BussinesRuleException, UnknownHostException{
		Customer customer = bt.save(input);
		return new ResponseEntity<>(customer,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Customer> delete(@PathVariable long id){
		 Optional<Customer> customer = customerRepository.findById(id);
		 customer.ifPresent(c->customerRepository.delete(c));
		 return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

}
