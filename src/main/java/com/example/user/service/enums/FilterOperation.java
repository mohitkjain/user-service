/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.enums;

public enum FilterOperation
{
	EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE;

	public static final String[] FILTER_OPERATION_SET = { ":", "!", ">", "<", "%" };

	public static FilterOperation getFilterOperation(String input)
	{
		switch (input)
		{
			case ":":
				return EQUALITY;
			case "!":
				return NEGATION;
			case ">":
				return GREATER_THAN;
			case "<":
				return LESS_THAN;
			case "%":
				return LIKE;
			default:
				return null;
		}
	}
}
