/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.exception;

import com.example.user.service.model.errors.ValidationError;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ValidationException extends RuntimeException
{
	private List<ValidationError> validationErrors;

	public ValidationException(ValidationError validationError)
	{
		this.validationErrors = Arrays.asList(validationError);
	}

	public ValidationException(List<ValidationError> validationErrors)
	{
		this.validationErrors = validationErrors;
	}
}
