/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.specification;

import com.example.user.service.entity.UserEntity;
import com.example.user.service.exception.ValidationException;
import com.example.user.service.model.FilterCriteria;
import com.example.user.service.model.errors.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity>
{
	private static final List<String> dateKeys = Arrays.asList("createdStamp", "lastUpdatedStamp");
	private FilterCriteria filterCriteria;

	/**
	 * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
	 * {@link Root} and {@link CriteriaQuery}.
	 *
	 * @param root            must not be {@literal null}.
	 * @param query           must not be {@literal null}.
	 * @param criteriaBuilder must not be {@literal null}.
	 * @return a {@link Predicate}, may be {@literal null}.
	 */
	@Nullable
	@Override
	public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
	{
		if (dateKeys.contains(filterCriteria.getKey()))
		{
			Date date;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				String dateString = String.valueOf(filterCriteria.getValue());
				date = dateFormat.parse(dateString);
			}
			catch (ParseException e)
			{
				ValidationError validationError = new ValidationError("filter." + filterCriteria.getKey(), "Invalid date provided");
				throw new ValidationException(validationError);
			}
			switch (filterCriteria.getOperation())
			{
				case EQUALITY:
				case LIKE:
					return criteriaBuilder.equal(criteriaBuilder.function("date", Date.class, root.<Date>get(filterCriteria.getKey())),
							date);
				case NEGATION:
					return criteriaBuilder.notEqual(criteriaBuilder.function("date", Date.class, root.<Date>get(filterCriteria.getKey())),
							date);
				case LESS_THAN:
					return criteriaBuilder.lessThan(root.<Date>get(filterCriteria.getKey()).as(Date.class), date);
				case GREATER_THAN:
					return criteriaBuilder.greaterThan(root.<Date>get(filterCriteria.getKey()).as(Date.class), date);
				default:
					return null;
			}
		}
		else
		{
			Path<Object> objectPath = root.get(filterCriteria.getKey());
			;
			switch (filterCriteria.getOperation())
			{
				case EQUALITY:
					return criteriaBuilder.equal(objectPath.as(String.class), String.valueOf(filterCriteria.getValue()));
				case NEGATION:
					return criteriaBuilder.notEqual(objectPath.as(String.class), String.valueOf(filterCriteria.getValue()));
				case LESS_THAN:
					return criteriaBuilder.lessThan(objectPath.as(String.class), String.valueOf(filterCriteria.getValue()));
				case GREATER_THAN:
					return criteriaBuilder.greaterThan(objectPath.as(String.class), String.valueOf(filterCriteria.getValue()));
				case LIKE:
					return criteriaBuilder.like(objectPath.as(String.class),
							'%' + String.valueOf(filterCriteria.getValue()).replace("_", "\\_") + '%');
				default:
					return null;
			}
		}
	}
}
