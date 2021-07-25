/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.dto;

import com.example.user.service.entity.UserEntity;
import com.example.user.service.enums.UserStatus;
import com.example.user.service.exception.NotFoundException;
import com.example.user.service.exception.ValidationException;
import com.example.user.service.model.errors.ValidationError;
import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.response.UserResponse;
import com.example.user.service.utility.JsonUtility;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

@Component
public class UserDTO
{
	static final Logger logger = LoggerFactory.getLogger(UserDTO.class);

	public UserEntity toUserEntity(AddUserRequest request)
	{
		if (request == null)
		{
			logger.error("request can not be null");
			ValidationError validationError = new ValidationError("addUserRequest", "addUserRequest can not be null");
			throw new ValidationException(validationError);
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setName(request.getName());
		userEntity.setMobile(request.getMobile());
		userEntity.setEmail(request.getEmail());

		UserStatus userStatus = EnumUtils.getEnum(UserStatus.class, request.getStatus());
		if (userStatus == null)
		{
			logger.error("user status can not be null");
			ValidationError validationError = new ValidationError("status", "status can not be null");
			throw new ValidationException(validationError);
		}
		userEntity.setStatus(userStatus);

		String dataJson = "{}";
		if (!CollectionUtils.isEmpty(request.getData()))
		{
			dataJson = JsonUtility.toJson(request.getData());
		}
		userEntity.setData(dataJson);

		return userEntity;
	}

	public UserResponse toUserResponse(UserEntity userEntity)
	{
		if (userEntity == null)
		{
			logger.error("user entity can not be null");
			throw new NotFoundException("user entity not found");
		}

		UserResponse userResponse = new UserResponse();
		userResponse.setUserId(userEntity.getUserId());
		userResponse.setName(userEntity.getName());
		userResponse.setMobile(userEntity.getMobile());
		userResponse.setEmail(userEntity.getEmail());
		userResponse.setStatus(userEntity.getStatus());

		Type type = new TypeToken<HashMap<String, String>>()
		{
		}.getType();
		HashMap<String, String> dataMap = JsonUtility.fromJson(userEntity.getData(), type);
		userResponse.setData(dataMap);

		userResponse.setCreated(userEntity.getCreatedStamp());
		userResponse.setLastUpdated(userEntity.getLastUpdatedStamp());

		return userResponse;
	}
}
