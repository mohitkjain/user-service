/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.utility;

import com.example.user.service.model.SortCriteria;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SpecificationUtils
{
	public <T, V extends Specification<T>> Optional<Specification<T>> andSpecification(List<V> criteria)
	{
		Iterator<V> itr = criteria.iterator();
		if (itr.hasNext())
		{
			Specification<T> spec = Specification.where(itr.next());
			while (itr.hasNext())
			{
				spec = spec.and(itr.next());
			}
			return Optional.of(spec);
		}
		return Optional.empty();
	}

	public <T, V extends Sort> Optional<Sort> andSort(List<V> criteria)
	{
		Iterator<V> itr = criteria.iterator();
		if (itr.hasNext())
		{
			Sort sort = (itr.next());
			while (itr.hasNext())
			{
				sort = sort.and(itr.next());
			}
			return Optional.of(sort);
		}
		return Optional.empty();
	}

	public List<Sort> generateSortList(List<SortCriteria> criteria)
	{
		return criteria.stream().map((sortCriteria) ->
		{
			switch (sortCriteria.getOperation())
			{
				case ASC:
					return Sort.by(Sort.Order.asc(sortCriteria.getKey()));
				case DESC:
					return Sort.by(Sort.Order.desc(sortCriteria.getKey()));
				default:
					return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
