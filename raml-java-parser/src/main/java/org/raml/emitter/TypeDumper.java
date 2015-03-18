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

import java.lang.reflect.Field;

import org.raml.model.Resource;
import org.raml.model.TemplateUse;

public class TypeDumper implements IRAMLFieldDumper{

	@Override
	public void dumpField(StringBuilder dump, int depth, Field declaredField,
			Object pojo, RamlEmitterV2 emitter) {
		Resource t=(Resource) pojo;
		TemplateUse typeModelT = t.getTypeModelT();
		if (typeModelT!=null){
			dump.append(emitter.indent(depth));
			dump.append("type: ");
			
			if (typeModelT.getParameters().isEmpty()){
				dump.append(typeModelT.getKey());
			}
			else{
				dump.append("{ ");
				dump.append(typeModelT);
				dump.append(" }");
			}
			dump.append("\n");
			return;
		}
	}

}
