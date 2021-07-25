/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.exception;

import lombok.Data;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
public class FieldValidationException extends RuntimeException
{
	private List<FieldError> fieldErrors;

	public FieldValidationException(Errors errors)
	{
		this.fieldErrors = errors.getFieldErrors();
	}
}
