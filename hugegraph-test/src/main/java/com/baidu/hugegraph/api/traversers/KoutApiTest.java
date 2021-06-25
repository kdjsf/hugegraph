/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.api.traversers;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baidu.hugegraph.api.BaseApiTest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class KoutApiTest extends BaseApiTest {

    final static String path = "graphs/hugegraph/traversers/kout";

    @Before
    public void prepareSchema() {
        BaseApiTest.initPropertyKey();
        BaseApiTest.initVertexLabel();
        BaseApiTest.initEdgeLabel();
        BaseApiTest.initVertex();
        BaseApiTest.initEdge();
    }

    @Test
    public void testGet() {
        Map<String, String> name2Ids = listAllVertexName2Ids();
        String markoId = name2Ids.get("marko");
        String peterId = name2Ids.get("peter");
        String joshId = name2Ids.get("josh");
        Response r = client().get(path,
                                  ImmutableMap.of("source", id2Json(markoId),
                                                  "max_depth", 2));
        String respBody = assertResponseStatus(200, r);
        List<String> vertices = assertJsonContains(respBody, "vertices");
        Assert.assertEquals(1, vertices.size());
        Assert.assertTrue(vertices.containsAll(ImmutableList.of(peterId,
                                                                joshId)));
    }

    @Test
    public void testPost() {
        Map<String, String> name2Ids = listAllVertexName2Ids();
        String markoId = name2Ids.get("marko");
        String reqBody = String.format("{ "
                                       + "\"source\": \"%s\", "
                                       + "\"step\": { "
                                       + "  \"direction\": \"BOTH\", "
                                       +
                                       "  \"labels\": [\"knows\", " +
                                       "\"created\"], "
                                       + "  \"properties\": { "
                                       + "    \"weight\": \"P.gt(0.1)\"}, "
                                       + "  \"degree\": 10000, "
                                       + "  \"skip_degree\": 100000}, "
                                       + "\"max_depth\": 1, "
                                       + "\"nearest\": true, "
                                       + "\"limit\": 10000, "
                                       + "\"with_vertex\": true, "
                                       + "\"with_path\": true}", markoId);
        Response resp = client().post(path, reqBody);
        String respBody = assertResponseStatus(200, resp);
        Map<String, Object> entity = parseMap(respBody);
        assertMapContains(entity, "size");
        assertMapContains(entity, "kout");
        assertMapContains(entity, "paths");
        assertMapContains(entity, "vertices");
        Assert.assertEquals(2, entity.get("size"));
    }
}
