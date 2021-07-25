/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.enums;

public enum SortOperation
{
	ASC, DESC;

	public static final String[] SORT_OPERATION_SET = { "asc", "desc" };

	public static SortOperation getSortOperation(String input)
	{
		switch (input)
		{
			case "asc":
				return ASC;
			case "desc":
				return DESC;
			default:
				return null;
		}
	}
}
