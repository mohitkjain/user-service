/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.parser;

import com.example.user.service.enums.FilterOperation;
import com.example.user.service.enums.SortOperation;
import com.example.user.service.model.FilterCriteria;
import com.example.user.service.model.SortCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CriteriaParser
{
	private String wordRegex = "[a-zA-Z]+";
	private String digitRegex = "[0-9]+";
	private String stringRegex = "[a-z\\-A-Z0-9\\s*_]+";
	private String filterOperatorRegex = "(:|<|>|!|%)";
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String sortOperatorRegex = "(_)";
	private String sortOperationRegex = "(asc|desc)";

	public Set<FilterCriteria> parseFilterString(String filterString)
	{
		Set<FilterCriteria> filterCriteriaSet = new HashSet<>();
		if (!StringUtils.isBlank(filterString))
		{
			String fullFilterRegex =
					"(" + wordRegex + ")" + filterOperatorRegex + "(" + stringRegex + "|" + dateRegex + "|" + digitRegex + "),";
			Pattern filterPattern = Pattern.compile(fullFilterRegex);
			Matcher matcher = filterPattern.matcher(filterString + ",");
			while (matcher.find())
			{
				FilterCriteria filterCriteria = new FilterCriteria();
				filterCriteria.setKey(matcher.group(1));
				filterCriteria.setOperation(FilterOperation.getFilterOperation(matcher.group(2)));
				filterCriteria.setValue(matcher.group(3));

				filterCriteriaSet.add(filterCriteria);
			}
		}
		return filterCriteriaSet;
	}

	public Set<SortCriteria> parseSortString(String sortString)
	{
		Set<SortCriteria> sortCriteriaSet = new HashSet<>();
		if (!StringUtils.isBlank(sortString))
		{
			String fullSortRegex = "(" + wordRegex + ")" + sortOperatorRegex + sortOperationRegex + ",";
			Pattern sortPattern = Pattern.compile(fullSortRegex);
			Matcher matcher = sortPattern.matcher(sortString + ",");
			while (matcher.find())
			{
				SortCriteria sortCriteria = new SortCriteria();
				sortCriteria.setKey(matcher.group(1));
				sortCriteria.setOperation(SortOperation.getSortOperation(matcher.group(3)));
				sortCriteriaSet.add(sortCriteria);
			}
		}
		return sortCriteriaSet;
	}
}
