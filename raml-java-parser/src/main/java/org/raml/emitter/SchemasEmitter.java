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
import java.util.Map;

import org.raml.model.Raml2;

public class SchemasEmitter implements IRAMLFieldDumper {

	@Override
	public void dumpField(StringBuilder dump, int depth, Field declaredField,
			Object pojo, RamlEmitterV2 emitter) {
		if (pojo instanceof Raml2){
		Raml2 rp=(Raml2) pojo;
		
		if (emitter.isSeparated){
			Map<String, String> schemaMap = rp.getSchemaMap();
			if (schemaMap.isEmpty()){
				return;
			}
			dump.append("schemas:\n");
			
			for (String q : schemaMap.keySet()) {
				dump.append(emitter.indent(depth + 1));
				dump.append("- ");
				dump.append(q);
				dump.append(": ");
				dump.append("!include");
				dump.append(' ');
				String str = schemaMap.get(q);
				dump.append(str);				
				dump.append("\n");
				if (emitter.writer != null) {
					String schemaContent = rp.getSchemaContent(q);
					if (schemaContent==null){
						schemaContent = rp.getSchemaContent(Character.toLowerCase(q.charAt(0))+q.substring(1));
					}
					emitter.writer.write(str,schemaContent);
				}
			}			
		}
		else{
			emitter.dumpSequenceField(dump, depth, declaredField,pojo);
		}
		}
		else{
			emitter.dumpSequenceField(dump, depth, declaredField,pojo);
		}
	}

}
