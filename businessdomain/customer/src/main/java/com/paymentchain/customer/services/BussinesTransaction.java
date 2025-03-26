package com.paymentchain.customer.services;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BussinesRuleException;
import com.paymentchain.customer.repository.CustomerRepository;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Service
public class BussinesTransaction {
	
	@Autowired
	CustomerRepository customerRepository;

    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.produtc.url}")
    private String productServiceUrl;
    
    @Value("${service.transaction.url}")
    private String transactionServiceUrl;
    
	public BussinesTransaction(WebClient.Builder webClientBuilder ) {
		this.webClientBuilder = webClientBuilder;
	}
	
	public Customer get(String code) {
		Customer customer = customerRepository.findByCode(code);
		List<CustomerProduct> products = customer.getProducts();
		
		if (products != null) {
			products.forEach(dto->{
				try {
					String name = getProductName(dto.getProductId());
					if(!name.isBlank()) {
						dto.setProductName(name);
					}
				} catch (UnknownHostException ex) {
					Logger.getLogger(BussinesTransaction.class.getName()).log(Level.SEVERE, null, ex);
				}
			});
			
		}
		
		customer.setTransactions(getTransacctions(customer.getIban()));

		return customer;
	}

    //definimos un timeout
	private final HttpClient httpClient = HttpClient
			.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.doOnConnected(connection -> {
				connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
			});

	private String getProductName(long id) throws UnknownHostException{
		String name = null;
		try {
			WebClient client = this.webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
					.baseUrl(productServiceUrl)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultUriVariables(Collections.singletonMap("url", productServiceUrl))
					.build();
			JsonNode block = client.method(HttpMethod.GET).uri("/"+id).retrieve().bodyToMono(JsonNode.class).block();
			name = block.get("name").asText();
		} catch (WebClientResponseException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				return "";
			} else {
				throw new UnknownHostException(e.getMessage());
			}
		}
		return name;
	}
	
    private <T> List<T> getTransacctions(String accountIban) {
        WebClient client = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(transactionServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", transactionServiceUrl))
                .build();        
        List<Object> block = client.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/transactions")
                .queryParam("ibanAccount", accountIban)               
                .build())
                .retrieve().bodyToFlux(Object.class).collectList().block();
        List<T> name = (List<T>) block;
        return name;
    }   

	public Customer save(Customer input) throws BussinesRuleException, UnknownHostException{
		if (input.getProducts() != null) {
			for (Iterator<CustomerProduct> it = input.getProducts().iterator(); it.hasNext();) {
                CustomerProduct dto = it.next();
				String name = getProductName(dto.getProductId());
				if (name.isBlank()) {
					BussinesRuleException bussienesException = new BussinesRuleException("1025", "Error de validacion, producto no existe", HttpStatus.PRECONDITION_FAILED);
					throw bussienesException;
				} else {
					dto.setCustomer(input);
				}
			};
		}
		
		Customer customer = customerRepository.save(input);	
		return customer;
	}

}
