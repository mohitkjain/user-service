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
import com.example.user.service.model.FilterCriteria;
import com.example.user.service.model.MetaData;
import com.example.user.service.model.SortCriteria;
import com.example.user.service.model.errors.ValidationError;
import com.example.user.service.model.request.AddUserRequest;
import com.example.user.service.model.request.UpdateUserRequest;
import com.example.user.service.model.response.BaseResponse;
import com.example.user.service.model.response.UserResponse;
import com.example.user.service.model.response.UserResponseList;
import com.example.user.service.repository.UserRepository;
import com.example.user.service.specification.UserSpecification;
import com.example.user.service.utility.SpecificationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SpecificationUtils specificationUtils;

	@Value("${users.data.desc.sort.default:createdStamp}")
	private String defaultSortKey;

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

	@Override
	public UserResponseList getAllUsers(
			int pageSize, int pageNo, List<FilterCriteria> filterCriteriaList, List<SortCriteria> sortCriteriaList)
	{
		List<Sort> sortList = specificationUtils.generateSortList(sortCriteriaList);
		Sort sort = specificationUtils.andSort(sortList).orElse(Sort.by(Sort.Order.desc(defaultSortKey)));

		logger.info("applying sorting on the query");
		//db page starts at 0
		pageNo = pageNo - 1;
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Optional<Specification<UserEntity>> specificationOptional = Optional.empty();
		if (!CollectionUtils.isEmpty(filterCriteriaList))
		{
			List<UserSpecification> specList = filterCriteriaList.stream().map(criteria -> new UserSpecification(criteria)).collect(
					Collectors.toList());
			specificationOptional = specificationUtils.andSpecification(specList);
		}

		Page<UserEntity> userEntityPage;
		if (specificationOptional.isPresent())
		{
			userEntityPage = userRepository.findAll(specificationOptional.get(), pageable);
			logger.info("fetching all records with matched filter in requested sort order");
		}
		else
		{
			userEntityPage = userRepository.findAll(pageable);
			logger.info("fetching all records in requested sort order");
		}
		List<UserEntity> userEntityList = userEntityPage.getContent();
		int currentPage = userEntityPage.getNumber() + 1;//page no starts at 0.
		int totalPages = userEntityPage.getTotalPages();
		int nextPage = currentPage >= totalPages ? -1 : currentPage + 1;

		MetaData metaData = new MetaData();
		metaData.setPage(currentPage);
		metaData.setPageSize(userEntityPage.getSize());
		metaData.setPageCount(totalPages);
		metaData.setTotalCount(userEntityPage.getTotalElements());
		metaData.setNextPage(nextPage);

		List<UserResponse> users = userEntityList.stream().map(userEntity -> userDTO.toUserResponse(userEntity)).collect(Collectors.toList());

		UserResponseList userResponseList = new UserResponseList();
		userResponseList.setUsers(users);
		userResponseList.setMetaData(metaData);
		return userResponseList;
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
