package com.paymentchain.products.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.products.entities.Product;
import com.paymentchain.products.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/product")
public class ProductRestController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Value("${user.role}")
	private String role;
	
    @Operation(description  = "Return all products bundled into Response", operationId = "List using GET")
    @ApiResponse(responseCode = "204", description = "There are not transactions")
    @ApiResponse(responseCode = "500", description  = "Internal error customerized")
	@GetMapping()
	public ResponseEntity<List<Product>> list(){
		List<Product> products = productRepository.findAll();
		if(products == null || products.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(products);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable Long id) {
		return productRepository.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Product> put(@PathVariable Long id, @RequestBody Product product){
		return null;
	}
	
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product input) {       
        Product save = productRepository.save(input);
        return ResponseEntity.ok(save);
   }
   
   @DeleteMapping("/{id}")
   public ResponseEntity<?> delete(@PathVariable Long id){
	   Optional<Product> deleted = productRepository.findById(id);
	   deleted.ifPresent(p -> productRepository.delete(p));
	   return deleted.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
	    	
}
