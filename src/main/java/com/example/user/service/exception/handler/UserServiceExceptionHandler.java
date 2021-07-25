/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.exception.handler;

import com.example.user.service.exception.FieldValidationException;
import com.example.user.service.exception.NotFoundException;
import com.example.user.service.exception.ValidationException;
import com.example.user.service.model.errors.UserServiceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class UserServiceExceptionHandler extends ResponseEntityExceptionHandler
{
	private final Logger logger = LoggerFactory.getLogger(UserServiceExceptionHandler.class);

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new UserServiceError(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(new UserServiceError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		UserServiceError apiError = new UserServiceError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addFieldValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addObjectValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(
			javax.validation.ConstraintViolationException ex)
	{
		UserServiceError apiError = new UserServiceError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		logger.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		String error = "Malformed JSON request";
		return buildResponseEntity(new UserServiceError(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(
			HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		String error = "Error writing JSON output";
		return buildResponseEntity(new UserServiceError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		UserServiceError apiError = new UserServiceError(BAD_REQUEST);
		apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle javax.persistence.EntityNotFoundException
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex)
	{
		return buildResponseEntity(new UserServiceError(HttpStatus.NOT_FOUND, ex));
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request)
	{
		if (ex.getCause() instanceof ConstraintViolationException)
		{
			return buildResponseEntity(new UserServiceError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new UserServiceError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request)
	{
		UserServiceError apiError = new UserServiceError(BAD_REQUEST);
		apiError.setMessage(String.format(
				"The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(),
				ex.getValue(),
				ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(UserServiceError apiError)
	{
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(value = { FieldValidationException.class })
	public ResponseEntity<Object> fieldValidationExceptionHandler(FieldValidationException ex)
	{
		UserServiceError apiError = new UserServiceError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addFieldValidationErrors(ex.getFieldErrors());

		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(value = { ValidationException.class })
	public ResponseEntity<Object> validationExceptionHandler(ValidationException ex)
	{
		UserServiceError apiError = new UserServiceError(NOT_ACCEPTABLE);
		apiError.setMessage("Not Acceptable error");
		apiError.addValidationErrors(ex.getValidationErrors());

		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(value = { NotFoundException.class })
	public ResponseEntity<Object> notFoundExceptionHandler(NotFoundException ex)
	{
		UserServiceError apiError = new UserServiceError(NOT_FOUND);
		apiError.setMessage("Not Found error");
		apiError.setDebugMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
}
