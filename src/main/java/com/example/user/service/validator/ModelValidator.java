/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.validator;

import com.example.user.service.exception.FieldValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ModelValidator
{
	public void handleValidationErrors(Errors errors) throws FieldValidationException
	{
		throw new FieldValidationException(errors);
	}
}
