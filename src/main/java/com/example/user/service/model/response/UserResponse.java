/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model.response;

import com.example.user.service.enums.UserStatus;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
public class UserResponse
{
	private Long userId;
	private String name;
	private String mobile;
	private String email;
	private UserStatus status;
	private HashMap<String, String> data;
	private Date created;
	private Date lastUpdated;
}
