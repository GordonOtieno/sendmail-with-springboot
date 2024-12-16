package com.gordon.gmail_sender.entity;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(content = Include.NON_DEFAULT)
public class HttpResponse {
	protected String timestamp;
	protected int statuscode;
	protected HttpStatus status;
	protected String message;
	protected String developerMessage;
	protected String path;
	protected String requestMethod;
	protected Map<?,?> data;
	

}
