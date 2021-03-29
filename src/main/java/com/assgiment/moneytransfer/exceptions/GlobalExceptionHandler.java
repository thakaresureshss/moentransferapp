package com.assgiment.moneytransfer.exceptions;

import com.assgiment.moneytransfer.errors.ErrorMessage;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 
 * @author suresh.thakare
 *
 *         Note :
 * 
 *         Handle all the exception individually not to use instance of in
 *         generic exception handler(Exception.class)
 * 
 *         Remove unused code autowired fields from class
 * 
 *         Return type should be specific not generic for each request
 * 
 */

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorMessage> handleBadRequest(BadRequestException ex, WebRequest request) {
		log.error("handling BadRequestException...");
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		log.error("handling ResourceNotFoundException...");
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorMessage> handleValidationExceptionException(ValidationException ex, WebRequest request) {
		log.error("handling ValidationException constraints Exception ...");
		ErrorMessage exception = new ErrorMessage(new Date(), "Validation failed.", request.getDescription(false),
				ex.getViolations());
		return new ResponseEntity<ErrorMessage>(exception, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		log.error("handling Integrity constraints Exception ...");
		ErrorMessage exception = new ErrorMessage(new Date(),
				"Invalid data provided in request or unique data required .", request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(exception, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleGlobalException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		log.error("handling Exception...");
		ErrorMessage exception = new ErrorMessage(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorMessage>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
