/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.controller;

import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.request.UpdateUserRequest;
import com.example.user.service.model.response.BaseResponse;
import com.example.user.service.model.response.UserResponse;
import com.example.user.service.service.UserService;
import com.example.user.service.utility.JsonUtility;
import com.example.user.service.validator.ModelValidator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
@Api(value = "User Controller")
public class UserController
{
	static final String mediaTypeVersion1 = "application/v1+json";

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ModelValidator modelValidator;

	@PostMapping(produces = mediaTypeVersion1)
	public ResponseEntity<UserResponse> addUser(
			@RequestBody @Valid AddUserRequest addUserRequest, @ApiIgnore Errors errors)
	{
		if (errors.hasErrors())
		{
			modelValidator.handleValidationErrors(errors);
		}

		logger.info("Received request in addUser: \n " + JsonUtility.toJson(addUserRequest));
		UserResponse userResponse = userService.addUser(addUserRequest);
		logger.info("Response: \n" + JsonUtility.toJson(userResponse));

		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@GetMapping(path = "/{userId}", produces = mediaTypeVersion1)
	public ResponseEntity<UserResponse> getUser(@PathVariable(name = "userId") String userId)
	{
		logger.info("Received request in getUser for userId: " + userId);
		UserResponse userResponse = userService.getUser(userId);
		logger.info("Response: \n" + JsonUtility.toJson(userResponse));

		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	@PutMapping(path = "/{userId}", produces = mediaTypeVersion1)
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable(name = "userId") String userId, @RequestBody @Valid UpdateUserRequest updateUserRequest, @ApiIgnore Errors errors)
	{
		if (errors.hasErrors())
		{
			modelValidator.handleValidationErrors(errors);
		}

		logger.info("Received request in updateUser : \n " + JsonUtility.toJson(updateUserRequest));
		UserResponse userResponse = userService.updateUser(userId, updateUserRequest);
		logger.info("Response: \n" + JsonUtility.toJson(userResponse));

		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{userId}", produces = mediaTypeVersion1)
	public ResponseEntity<BaseResponse> deleteUser(
			@PathVariable(name = "userId") String userId)
	{
		logger.info("Received request in deleteUser for userId: " + userId);
		BaseResponse baseResponse = userService.deleteUser(userId);
		logger.info("Response: \n" + JsonUtility.toJson(baseResponse));

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}
}
