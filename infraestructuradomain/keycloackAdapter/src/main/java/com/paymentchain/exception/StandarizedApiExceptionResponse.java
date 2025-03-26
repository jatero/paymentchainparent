/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.exception;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jatero
 The effort to standardize rest API error reports  is support by ITEF 
 (Internet Engineering Task Force, open standard organization  that which develop and promotes voluntary internet standards) 
 in RFC 7807 which created a generalized error-handling schema composed by five parts.
	1- type     — A URI identifier that categorizes the error
	2- title    — A brief, human-readable message about the error
	3- code     — The unique error code
	4- detail   — A human-readable explanation of the error
	5- instance — A URI that identifies the specific occurrence of the error
 Standarized is optional but have advantage, it is use for facebook and twitter ie
 	https://graph.facebook.com/oauth/access_token?
 * https://api.twitter.com/1.1/statuses/update.json?include_entities=true
 */
@Getter
@Setter
public class StandarizedApiExceptionResponse{
    private String type ="/errors/uncategorized";  
    private String title;   
    private String code;    
    private String detail;
    private String instance ="/errors/uncategorized/bank";

    public StandarizedApiExceptionResponse(String title, String code, String detail) {
        super();
        this.title = title;
        this.code = code;
        this.detail = detail;
    }       
       
}
