/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtility
{
	private JsonUtility()
	{
	}

	public static String toJson(Object object)
	{
		return toJson(object, object.getClass());
	}

	public static String toJson(Object object, Class clazz)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(object, clazz);
	}

	public static <T> T fromJson(String data, Type type)
	{
		Gson gson = new Gson();
		return gson.fromJson(data, type);
	}
}
