/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model.response;

import com.example.user.service.model.MetaData;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseList
{
	private List<UserResponse> users;
	private MetaData metaData;
}
