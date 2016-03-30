/**
 * Copyright 2014 Sciamlab s.r.l.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sciamlab.common.exception.web;

import javax.ws.rs.core.Response;

/**
 * User: porter
 * Date: 03/05/2012
 * Time: 12:27
 */
public class NotFoundException extends SciamlabWebApplicationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5212139422947303994L;

	private static final Response.Status status = Response.Status.NOT_FOUND;
	
	public NotFoundException() {
        this(null);
    }
	
	public NotFoundException(String applicationMessage) {
		super(status.getStatusCode(), status.getStatusCode(), status.getReasonPhrase(), applicationMessage);
    }
}
