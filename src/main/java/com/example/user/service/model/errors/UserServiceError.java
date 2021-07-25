/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model.errors;

import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class UserServiceError
{
	private HttpStatus status;
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<UserServiceSubError> subErrors;

	private UserServiceError()
	{
		timestamp = LocalDateTime.now();
	}

	public UserServiceError(HttpStatus status)
	{
		this();
		this.status = status;
	}

	public UserServiceError(HttpStatus status, Throwable ex)
	{
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public UserServiceError(HttpStatus status, String message, Throwable ex)
	{
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	private void addSubError(UserServiceSubError subError)
	{
		if (subErrors == null)
		{
			subErrors = new ArrayList<>();
		}
		subErrors.add(subError);
	}

	private void addFieldValidationError(String object, String field, Object rejectedValue, String message)
	{
		addSubError(new FieldValidationError(object, field, rejectedValue, message));
	}

	private void addValidationError(String object, String message)
	{
		addSubError(new ValidationError(object, message));
	}

	private void addFieldValidationError(FieldError fieldError)
	{
		this.addFieldValidationError(fieldError.getObjectName(),
				fieldError.getField(),
				fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

	public void addFieldValidationErrors(List<FieldError> fieldErrors)
	{
		fieldErrors.forEach(this::addFieldValidationError);
	}

	private void addObjectValidationError(ObjectError objectError)
	{
		this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
	}

	public void addObjectValidationError(List<ObjectError> globalErrors)
	{
		globalErrors.forEach(this::addObjectValidationError);
	}

	private void addValidationError(ConstraintViolation<?> cv)
	{
		this.addFieldValidationError(cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
				cv.getInvalidValue(),
				cv.getMessage());
	}

	public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations)
	{
		constraintViolations.forEach(this::addValidationError);
	}

	public void addValidationErrors(List<ValidationError> validationErrors)
	{
		validationErrors.forEach(this::addValidationError);
	}

	private void addValidationError(ValidationError validationError)
	{
		this.addValidationError(validationError.getObject(), validationError.getMessage());
	}
}
