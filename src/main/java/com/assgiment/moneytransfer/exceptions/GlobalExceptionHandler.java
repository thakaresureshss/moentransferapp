package com.assgiment.moneytransfer.exceptions;

import com.assgiment.moneytransfer.errors.ErrorMessage;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
@Validated
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	ApplicationContext context;

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequest(BadRequestException ex, WebRequest request) {
		log.debug("handling BadRequestException...");
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		log.debug("handling ResourceNotFoundException...");
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		log.debug("handling Exception...");
		if (ex instanceof DataIntegrityViolationException) {
			ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
			return new ResponseEntity(exception, HttpStatus.BAD_REQUEST);
		}
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
