/*
 * Copyright 2012-2016 the original author or authors.
 *
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
 */
package com.wolfsoftco.httpclient;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Rest Client for the Spring Boot Security OAuth2 Sample
 * 
 * URL Project: http://projects.spring.io/spring-boot/
 * 
 * @author farith heras
 * @farithjhg
 *
 */
public class RestClient {
    private static final String URL = "http://localhost:9090/oauth/token";
    private static final String URL_FLIGHTS = "http://localhost:9090/flights/1";

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpEntity entity = null;
            String responseHtml = null;
            httpclient = HttpClients.custom().build();
            
            //Based on CURL line:
            //curl localhost:9090/oauth/token -d "grant_type=password&scope=write&username=greg&password=turnquist" -u foo:bar
            
            //clientid:secret
            String clientID = "foo:bar";

            HttpUriRequest login = RequestBuilder.post().setUri(new URI(URL))
                    .addHeader("Authorization", "Basic " + Base64.encodeBase64String(clientID.getBytes()))
                    .addParameter("grant_type", "password").addParameter("scope", "read")
                    .addParameter("username", "greg").addParameter("password", "turnquist").build();

            response = httpclient.execute(login);
            entity = response.getEntity();

            responseHtml = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            
            JsonObject jsonObject = null;
            String token = null;
            if (responseHtml.startsWith("{")) {
                JsonParser parser = new JsonParser();
                jsonObject = parser.parse(responseHtml).getAsJsonObject();
                token = jsonObject.get("access_token").getAsString();
            }

            if (token != null) {
                HttpGet request = new HttpGet(URL_FLIGHTS);
                request.addHeader("Authorization", "Bearer " + token);

                response = httpclient.execute(request);
                entity = response.getEntity();
                responseHtml = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                System.out.println(responseHtml);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {

                if (httpclient != null)
                    httpclient.close();
                if (response != null)
                    response.close();

            } catch (IOException e) {
            }
        }
    }
}
