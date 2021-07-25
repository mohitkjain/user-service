/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.service;

import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.request.UpdateUserRequest;
import com.example.user.service.model.response.BaseResponse;
import com.example.user.service.model.response.UserResponse;

public interface UserService
{
	UserResponse addUser(AddUserRequest addUserRequest);

	UserResponse getUser(String userId);

	UserResponse updateUser(String userId, UpdateUserRequest updateUserRequest);

	BaseResponse deleteUser(String userId);
}
