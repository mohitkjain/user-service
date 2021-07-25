/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.validator;

import com.example.user.service.enums.FilterOperation;
import com.example.user.service.enums.SortOperation;
import com.example.user.service.exception.FieldValidationException;
import com.example.user.service.exception.ValidationException;
import com.example.user.service.model.FilterCriteria;
import com.example.user.service.model.SortCriteria;
import com.example.user.service.model.errors.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelValidator
{
	private Logger logger = LoggerFactory.getLogger(ModelValidator.class);

	public void handleValidationErrors(Errors errors) throws FieldValidationException
	{
		throw new FieldValidationException(errors);
	}

	public void validateFilterAndPaginationValues(
			String perPage,
			String page,
			String filterStr,
			String sort,
			Set<FilterCriteria> filterCriteriaSet,
			Set<SortCriteria> sortCriteriaSet,
			Map<String, String> validUsersFilterCriteria,
			Map<String, String> validUsersSortCriteria)
	{
		validatePageRequest(perPage, page);
		Set<String> validFilterKeys = validUsersFilterCriteria.keySet();
		Set<String> validSortKeys = validUsersSortCriteria.keySet();

		Set<String> notMatchedFilter = filterCriteriaSet.stream().filter(filterCriteria -> !validFilterKeys.contains(filterCriteria.getKey())).map(
				FilterCriteria::getKey).collect(Collectors.toSet());

		Set<String> notMatchedSort = sortCriteriaSet.stream().filter(sortCriteria -> !validSortKeys.contains(sortCriteria.getKey())).map(
				SortCriteria::getKey).collect(Collectors.toSet());

		validateSortFilterSize(notMatchedFilter.size(), notMatchedSort.size(), validFilterKeys, validSortKeys);

		if (StringUtils.isNotBlank(filterStr))
		{
			int filterStringsCount = filterStr.split(",", -1).length;
			if (filterStringsCount != filterCriteriaSet.size())
			{
				String defaultMessage = "please provide valid filter string like {key}{operation}{value}, key can be:" + validFilterKeys
						+ " operation can be " + Arrays.toString(FilterOperation.FILTER_OPERATION_SET);
				logger.error(defaultMessage);

				ValidationError validationError = new ValidationError("filter", defaultMessage);
				throw new ValidationException(validationError);
			}
		}
		if (StringUtils.isNotBlank(sort))
		{
			int sortStringsCount = sort.split(",", -1).length;
			if (sortStringsCount != sortCriteriaSet.size())
			{
				String defaultMessage = "please provide unique/valid sort string like {key}_{operation}, key can be: " + validSortKeys
						+ " operation can be " + Arrays.toString(SortOperation.SORT_OPERATION_SET);
				logger.error(defaultMessage);

				ValidationError validationError = new ValidationError("sort", defaultMessage);
				throw new ValidationException(validationError);
			}
		}
	}

	private void validatePageRequest(String perPage, String page)
	{
		List<ValidationError> validationErrors = new ArrayList<>();
		String defaultMessage;
		try
		{
			if (StringUtils.isNotBlank(perPage) && Integer.valueOf(perPage).compareTo(0) < 0)
			{
				defaultMessage = "perPage should be a positive number";
				logger.error(defaultMessage);
				ValidationError validationError = new ValidationError("perPage", defaultMessage);
				validationErrors.add(validationError);
			}
			if (StringUtils.isNotBlank(page) && Integer.valueOf(page).compareTo(0) < 0)
			{
				defaultMessage = "page should be a positive number";
				logger.error(defaultMessage);
				ValidationError validationError = new ValidationError("perPage", defaultMessage);
				validationErrors.add(validationError);
			}
		}
		catch (NumberFormatException e)
		{
			defaultMessage = "Pagination values should be of type number";
			logger.error(defaultMessage);
			ValidationError validationError = new ValidationError("page,perPage", defaultMessage);
			validationErrors.add(validationError);
		}
		if (validationErrors.size() > 0)
		{
			throw new ValidationException(validationErrors);
		}
	}

	private void validateSortFilterSize(int filterSize, int sortSize, Set<String> validFilterKeys, Set<String> validSortKeys)
	{
		List<ValidationError> validationErrors = new ArrayList<>();
		String defaultMessage;
		if (filterSize != 0)
		{
			defaultMessage = "filter key is not valid, valid keys are " + validFilterKeys;
			logger.error(defaultMessage);
			ValidationError validationError = new ValidationError("filter", defaultMessage);
			validationErrors.add(validationError);
		}

		if (sortSize != 0)
		{
			defaultMessage = "sort key is not valid, valid keys are " + validSortKeys;
			logger.error(defaultMessage);
			ValidationError validationError = new ValidationError("sort", defaultMessage);
			validationErrors.add(validationError);
		}
		if (validationErrors.size() > 0)
		{
			throw new ValidationException(validationErrors);
		}
	}
}
