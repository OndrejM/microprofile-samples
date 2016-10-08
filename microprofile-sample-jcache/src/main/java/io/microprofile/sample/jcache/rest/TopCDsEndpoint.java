/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.microprofile.sample.jcache.rest;

/*-
 * #%L
 * Microprofile Samples :: Canonical
 * %%
 * Copyright (C) 2016 Microprofile.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.microprofile.sample.jcache.model.CD;
import io.microprofile.sample.jcache.persistence.CDRepository;
import io.microprofile.sample.jcache.utils.QLogger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
@RequestScoped
public class TopCDsEndpoint {

    @Inject
    @QLogger
    private Logger logger;
    
    @Inject
    private CDRepository cdRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTopCDs() {

        final JsonArrayBuilder array = Json.createArrayBuilder();
        final List<CD> topCDs = cdRepo.getTopCDs();
        for (final CD aCD : topCDs) {
            array.add(Json.createObjectBuilder().add("id", aCD.getId()));
        }
        return array.build().toString();
    }

}
