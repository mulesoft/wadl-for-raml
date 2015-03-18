/*
 * Copyright (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.emitter;

import org.raml.model.ParamType;
import org.raml.model.parameter.UriParameter;

public class UrlParameterFilter implements IFilter<UriParameter>{

	@Override
	public boolean accept(UriParameter element) {
		if (element.getType()==ParamType.STRING){
			if (element.getDescription()==null||element.getDescription().trim().length()==0){
				if (element.getEnumeration()==null||element.getEnumeration().isEmpty()){
					if (element.getPattern()==null||element.getPattern().isEmpty()){
						return false;
					}
				}
			}
		}
		return true;
	}
}
