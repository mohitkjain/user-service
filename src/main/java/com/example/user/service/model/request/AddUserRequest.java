/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.model.request;

import com.example.user.service.annotations.EnumValidator;
import com.example.user.service.enums.UserStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashMap;

@Data
public class AddUserRequest
{
	@NotBlank(message = "Name can't be blank")
	@ApiModelProperty(example = "Mohit Jain")
	private String name;

	@ApiModelProperty(example = "9999888899")
	@NotBlank(message = "Mobile number can not be blank")
	@Pattern(regexp = "\\d+", message = "Mobile Number should be non-blank and numeric")
	private String mobile;

	@ApiModelProperty(example = "web@user.com")
	@NotBlank(message = "Email can not be blank")
	@Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Email Id is not in valid format")
	private String email;

	@ApiModelProperty(example = "ACTIVE")
	@EnumValidator(enumClass = UserStatus.class, message = "Status can be ACTIVE/INACTIVE")
	private String status;

	@ApiModelProperty(example = "{\n" + "\"key\": \"value\" \n}")
	private HashMap<String, String> data;
}
