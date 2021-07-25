/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model;

import lombok.Data;

@Data
public class MetaData
{
	private int page;
	private int nextPage;
	private int pageSize;
	private int pageCount;
	private long totalCount;
}
