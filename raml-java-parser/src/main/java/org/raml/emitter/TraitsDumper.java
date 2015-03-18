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
import java.util.List;

import org.raml.model.Action;
import org.raml.model.Resource;
import org.raml.model.TemplateUse;

public class TraitsDumper implements IRAMLFieldDumper {

	@Override
	public void dumpField(StringBuilder dump, int depth, Field declaredField,
			Object pojo, RamlEmitterV2 emitter) {
		List<TemplateUse> resourceTypeMap = null;
		if (pojo instanceof Action) {
			Action v = (Action) pojo;
			resourceTypeMap = (List<TemplateUse>) v.getIsModel();
		}
		if (pojo instanceof Resource) {
			Resource v = (Resource) pojo;
			resourceTypeMap = (List<TemplateUse>) v.getIsModel();
		}
		if (resourceTypeMap.isEmpty()) {
			return;
		}
		dump.append(emitter.indent(depth));
		dump.append("is: ");
		if (false) {
			dump.append(resourceTypeMap.iterator().next());
			return;
		} else {
			int a = 0;
			dump.append("[ ");
			for (TemplateUse t : resourceTypeMap) {
				dump.append(t);
				a++;
				if (a < resourceTypeMap.size()) {
					dump.append(" , ");
				}
			}
			dump.append(" ]");
		}
		dump.append("\n");

	}

}
