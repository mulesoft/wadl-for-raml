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
package org.raml.parser.rule;

import java.util.List;
import java.util.Map;

import org.raml.model.Raml2;
import org.raml.parser.annotation.ExtraHandler;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class GlobalSchemasHandler implements ExtraHandler {

	@Override
	public void handle(Object pojo, SequenceNode node) {
		if (pojo instanceof Raml2) {
			Raml2 r=(Raml2) pojo;
			Map<String, String> schemaMap = r.getSchemaMap();
			for (Node n : node.getValue()) {
				if (n instanceof MappingNode) {
					MappingNode m = (MappingNode) n;
					List<NodeTuple> value = m.getValue();
					for (NodeTuple t : value) {
						Node keyNode = t.getKeyNode();
						if (keyNode instanceof ScalarNode){
							ScalarNode sc=(ScalarNode) keyNode;
							String value2 = sc.getValue();
							
							Node valueNode = t.getValueNode();
							if (valueNode instanceof ScalarNode){
								ScalarNode sm=(ScalarNode) valueNode;
								if (sm.getTag().getValue().equals("!include")){
									schemaMap.put(value2,sm.getValue());
								}
							}
						}
						
					}
				}
			}
		}
	}

}
