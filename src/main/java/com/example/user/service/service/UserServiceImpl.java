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
import com.example.user.service.model.request.UpdateUserRequest;
import com.example.user.service.model.response.BaseResponse;
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
		UserResponse userResponse = userDTO.toUserResponse(getUserEntity(userId));
		return userResponse;
	}

	@Override
	public UserResponse updateUser(String userId, UpdateUserRequest updateUserRequest)
	{
		UserEntity userEntity = getUserEntity(userId);
		userDTO.updateUserEntity(userEntity, updateUserRequest);

		logger.info("Updating userEntity into DB");
		userEntity = userRepository.save(userEntity);
		logger.info("Updated userEntity into DB");

		UserResponse userResponse = userDTO.toUserResponse(userEntity);
		return userResponse;
	}

	@Override
	public BaseResponse deleteUser(String userId)
	{
		UserEntity userEntity = getUserEntity(userId);

		logger.info("Deleting userEntity from DB with userId " + userId);
		userRepository.delete(userEntity);
		logger.info("Deleted userEntity from DB with userId " + userId);

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("User has been deleted successfully with userId " + userId);
		return baseResponse;
	}

	private UserEntity getUserEntity(String userId)
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
		if (!userEntityOptional.isPresent())
		{
			logger.error("user not found with userId " + userIdLong);
			throw new NotFoundException("user not found with userId " + userIdLong);
		}
		logger.info("Fetched userEntity from DB for userId " + userIdLong);
		return userEntityOptional.get();
	}
}
