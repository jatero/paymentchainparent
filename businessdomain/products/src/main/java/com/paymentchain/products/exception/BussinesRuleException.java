package com.paymentchain.products.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BussinesRuleException extends Exception {

	private static final long serialVersionUID = 6705726301368528297L;
    private long id;
    private String code;   
    private HttpStatus httpStatus;
    
	public BussinesRuleException(long id, String code, String message, HttpStatus httpStatus) {
		super(message);
		this.id = id;
		this.code = code;
		this.httpStatus = httpStatus;
	}

    public BussinesRuleException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }
    
    public BussinesRuleException(String message, Throwable cause) {
        super(message, cause);
    }     
    
}
