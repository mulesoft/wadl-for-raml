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
package org.raml.parser.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.raml.model.TemplateUse;
import org.raml.parser.annotation.ExtraHandler;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class TemplatesExtraHandler implements ExtraHandler {

	String field;
	
	public TemplatesExtraHandler(String field) {
		super();
		this.field = field;
	}

	@Override
	public void handle(Object pojo, SequenceNode node) {
		List<Node> value = node.getValue();
		ArrayList<TemplateUse> str = new ArrayList<TemplateUse>();
		for (Node n : value) {
			if (n instanceof ScalarNode) {
				
				str.add(new TemplateUse(((ScalarNode) n).getValue()));
			}
			if (n instanceof MappingNode) {
				MappingNode m = (MappingNode) n;
				List<NodeTuple> value2 = m.getValue();
				for (NodeTuple q : value2) {
					TemplateUse t = null;
					Node keyNode = q.getKeyNode();
					if (keyNode instanceof ScalarNode) {
						String value3 = ((ScalarNode) keyNode).getValue();
						t = new TemplateUse(value3);
					}
					Node valueNode = q.getValueNode();

					if (valueNode instanceof MappingNode) {
						MappingNode zz = (MappingNode) valueNode;
						List<NodeTuple> value3 = zz.getValue();
						for (NodeTuple ma : value3) {
							Node keyNode2 = ma.getKeyNode();
							Node valueNode2 = ma.getValueNode();
							if (keyNode2 instanceof ScalarNode
									&& valueNode2 instanceof ScalarNode) {
								ScalarNode scalarNode = (ScalarNode) valueNode2;
								String value4 = scalarNode.getValue();
								if (scalarNode.getTag().equals(new Tag("!include"))){
									value4="!include "+value4;
								}								
								t.getParameters().put(
										((ScalarNode) keyNode2).getValue(),
										value4);
							} else {
								throw new IllegalStateException();
							}
						}

					}
					if (t != null) {
						str.add(t);
					}
				}
			}
		}
		try {
			PropertyUtils.setProperty(pojo, field, str);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
