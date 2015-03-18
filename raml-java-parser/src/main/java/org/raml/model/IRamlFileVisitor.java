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

import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.raml.model.parameter.UriParameter;


public interface IRamlFileVisitor {

	public boolean startVisit(Resource resource);
	
	public void endVisit(Resource resource);
	
	public boolean startVisit(Action action);
	
	public boolean endVisit(Action action);
	
	public void visit(String name,QueryParameter queryParameter);
	
	public void visit(String name,UriParameter uriParameter);
	
	public void visit(String name,Header header);
	
	public boolean startVisit(String code,Response response);
	public void endVisit(Response response);
	
	public void visit(MimeType mimeType);
	
	public boolean startVisitBody();
	
	public void endVisitBody();
	
	public void visitTrait(Action traitModel);
	
	public void visitResourceType(Resource typeModel);
}
