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
package org.raml.model;

import java.util.LinkedHashMap;

public class TemplateUse {

	protected String key;
	protected LinkedHashMap<String, String>parameters=new LinkedHashMap<String, String>();
	public TemplateUse(String value) {
		this.key=value;
	}
	public TemplateUse() {
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public LinkedHashMap<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(LinkedHashMap<String, String> parameters) {
		this.parameters = parameters;
	}	
	
	@Override
	public String toString() {
		if (parameters.isEmpty()){
			return key;
		}
		else{
			StringBuilder bld=new StringBuilder();
			bld.append(key);
			bld.append(':');
			bld.append(' ');
			bld.append('{');
			int a=0;
			for (String s: parameters.keySet()){
				bld.append(' ');
				bld.append(s);
				bld.append(": ");
				String str = parameters.get(s);
				if (str.startsWith("!include")){
					str="\""+str+"\"";
				}
				bld.append(str);
				a++;
				if (a!=parameters.size()){
					bld.append(',');
				}
			}
			bld.append(' ');
			bld.append('}');
			return bld.toString();
		}	
	}
}
