/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model;

import com.example.user.service.enums.FilterOperation;
import lombok.Data;

@Data
public class FilterCriteria
{
	private String key;
	private FilterOperation operation;
	private String value;

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		FilterCriteria that = (FilterCriteria) o;

		return key.equals(that.key);
	}

	@Override
	public int hashCode()
	{
		return key.hashCode();
	}
}
