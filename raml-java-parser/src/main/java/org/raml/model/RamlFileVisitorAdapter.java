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

import org.raml.model.Action;
import org.raml.model.MimeType;
import org.raml.model.Resource;
import org.raml.model.Response;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.raml.model.parameter.UriParameter;

public class RamlFileVisitorAdapter implements IRamlFileVisitor {

	@Override
	public boolean startVisit(Resource resource) {
		return true;
	}

	@Override
	public void endVisit(Resource resource) {

	}

	@Override
	public boolean startVisit(Action action) {
		return true;
	}

	@Override
	public boolean endVisit(Action action) {
		return true;
	}

	@Override
	public void visit(String name, QueryParameter queryParameter) {

	}

	@Override
	public void visit(String name, UriParameter uriParameter) {

	}

	@Override
	public void visit(String name, Header header) {

	}

	@Override
	public boolean startVisit(String code, Response response) {
		return true;
	}

	@Override
	public void endVisit(Response response) {

	}

	@Override
	public void visit(MimeType mimeType) {

	}

	@Override
	public boolean startVisitBody() {
		return true;
	}

	@Override
	public void endVisitBody() {

	}

	public void visitTrait(Action traitModel) {

	}

	@Override
	public void visitResourceType(Resource typeModel) {

	}

}