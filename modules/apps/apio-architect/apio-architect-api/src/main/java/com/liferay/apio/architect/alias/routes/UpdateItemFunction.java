/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.architect.alias.routes;

import static com.liferay.apio.architect.operation.Method.*;

import com.liferay.apio.architect.alias.RequestFunction;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.operation.Method;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.function.Function;

/**
 * Defines a type alias for a function that can be used to update a collection
 * item.
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @param  <S> the type of the model's identifier (e.g., {@code Long}, {@code
 *         String}, etc.)
 */
@FunctionalInterface
public interface UpdateItemFunction<T, S>
	extends RequestFunction<Function<S, Function<Body, Try<SingleModel<T>>>>> {

	@Override
	public default Method getMethod() {
		return PUT;
	}

}