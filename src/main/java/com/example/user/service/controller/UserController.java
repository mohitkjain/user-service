/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.controller;

import com.example.user.service.model.FilterCriteria;
import com.example.user.service.model.SortCriteria;
import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.request.UpdateUserRequest;
import com.example.user.service.model.response.BaseResponse;
import com.example.user.service.model.response.UserResponse;
import com.example.user.service.model.response.UserResponseList;
import com.example.user.service.parser.CriteriaParser;
import com.example.user.service.service.UserService;
import com.example.user.service.utility.JsonUtility;
import com.example.user.service.validator.ModelValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

	@Autowired
	private CriteriaParser criteriaParser;

	@Value("#{${users.fetch.filter.map}}")
	private Map<String, String> validUsersFilterCriteria;

	@Value("#{${users.fetch.sort.map}}")
	private Map<String, String> validUsersSortCriteria;

	@Value("${oms.data.fetch.default.perPage:30}")
	private int defaultPerPage;

	@Value("${oms.data.fetch.default.page:1}")
	private int defaultPage;

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

	@GetMapping(produces = mediaTypeVersion1)
	public ResponseEntity<UserResponseList> getAllUsers(
			@RequestParam(value = "perPage", required = false) @ApiParam(value = "specifies the page size") String perPage,
			@RequestParam(value = "page", required = false) @ApiParam(value = "specifies the page no") String page,
			@RequestParam(value = "sort", required = false) @ApiParam(value = "specifies the sort query") String sort,
			@RequestParam(value = "filter", required = false) @ApiParam(value = "specifies the filter") String filterStr)
	{

		logger.info("Received GET request in getAllUsers method.");

		Set<FilterCriteria> filterCriteriaSet = criteriaParser.parseFilterString(filterStr);
		Set<SortCriteria> sortCriteriaSet = criteriaParser.parseSortString(sort);

		logger.info("validating filter and sort criteria");
		modelValidator.validateFilterAndPaginationValues(
				perPage,
				page,
				filterStr,
				sort,
				filterCriteriaSet,
				sortCriteriaSet,
				validUsersFilterCriteria,
				validUsersSortCriteria);
		logger.info("validated filter and sort criteria");

		List<FilterCriteria> filterCriteriaList = filterCriteriaSet.stream().map(filterCriteria ->
		{
			filterCriteria.setKey(validUsersFilterCriteria.get(filterCriteria.getKey()));
			return filterCriteria;
		}).collect(Collectors.toList());

		List<SortCriteria> sortCriteriaList = sortCriteriaSet.stream().map(sortCriteria ->
		{
			sortCriteria.setKey(validUsersSortCriteria.get(sortCriteria.getKey()));
			return sortCriteria;
		}).collect(Collectors.toList());

		int pageSize = StringUtils.isBlank(perPage) ? defaultPerPage : Integer.parseInt(perPage);
		int pageNo = StringUtils.isBlank(page) ? defaultPage : Integer.parseInt(page);

		UserResponseList userResponseList = userService.getAllUsers(pageSize, pageNo, filterCriteriaList, sortCriteriaList);
		return new ResponseEntity<>(userResponseList, HttpStatus.OK);
	}
}
