/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.service;

import com.example.user.service.dto.UserDTO;
import com.example.user.service.entity.UserEntity;
import com.example.user.service.exception.NotFoundException;
import com.example.user.service.exception.ValidationException;
import com.example.user.service.model.errors.ValidationError;
import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.response.UserResponse;
import com.example.user.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponse addUser(AddUserRequest addUserRequest)
	{
		UserEntity userEntity = userDTO.toUserEntity(addUserRequest);

		logger.info("Saving userEntity into DB");
		userEntity = userRepository.save(userEntity);
		logger.info("Saved userEntity into DB");

		UserResponse userResponse = userDTO.toUserResponse(userEntity);

		return userResponse;
	}

	@Override
	public UserResponse getUser(String userId)
	{
		Long userIdLong;
		try
		{
			userIdLong = Long.valueOf(userId);
		}
		catch (Exception ex)
		{
			logger.error("userId can not converted into long");
			ValidationError validationError = new ValidationError("userId", "userId should be numeric and positive");
			throw new ValidationException(validationError);
		}

		Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userIdLong);
		if(!userEntityOptional.isPresent())
		{
			logger.error("user not found with userId " + userIdLong);
			throw new NotFoundException("user not found with userId " + userIdLong);
		}
		logger.info("Fetched userEntity from DB for userId " + userIdLong);
		UserResponse userResponse = userDTO.toUserResponse(userEntityOptional.get());

		return userResponse;
	}
}
