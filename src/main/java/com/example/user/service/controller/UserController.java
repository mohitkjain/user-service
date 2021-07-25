/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.controller;

import com.example.user.service.model.request.AddUserRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
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
		logger.info("Received request in addUser, request: \n " + JsonUtility.toJson(addUserRequest));

		UserResponse userResponse = userService.addUser(addUserRequest);

		logger.info("Response: \n" + JsonUtility.toJson(userResponse));

		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}
}
