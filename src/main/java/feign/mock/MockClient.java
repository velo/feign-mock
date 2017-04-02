/**
 * Copyright (C) 2016 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feign.mock;

import static feign.Util.UTF_8;
import static feign.Util.toByteArray;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;

public class MockClient implements Client {

    private static final Map<String, Collection<String>> NO_HEADERS = new HashMap<String, Collection<String>>();

    private static final int HTTP_NO_CONTENT = 204;
    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 404;

    private final Map<RequestKey, Response.Builder> responses = new HashMap<>();
    private final Map<RequestKey, List<Request>> requests = new HashMap<>();

    @Override
    public Response execute(Request request, Options options) throws IOException {
        RequestKey key = new RequestKey(HttpMethod.valueOf(request.method()),
                URLDecoder.decode(request.url(), UTF_8.name()));

        if (requests.containsKey(key))
            requests.get(key).add(request);
        else
            requests.put(key, new ArrayList<>(asList(request)));

        if (responses.containsKey(key))
            return responses.get(key)
                    .request(request)
                    .build();

        return Response.builder()
                .status(HTTP_NOT_FOUND)
                .reason("Not mocker")
                .headers(request.headers())
                .request(request)
                .build();
    }

    public MockClient ok(HttpMethod method, String url, InputStream input) throws IOException {
        return ok(method, url, toByteArray(input));
    }

    public MockClient ok(HttpMethod method, String url, String text) {
        return ok(method, url, text.getBytes());
    }

    public MockClient noContent(HttpMethod method, String url) {
        return add(method, url, Response.builder()
                .status(HTTP_NO_CONTENT)
                .reason("Mocked")
                .headers(NO_HEADERS));
    }

    public MockClient ok(HttpMethod method, String url, byte[] data) {
        return add(method, url, Response.builder()
                .status(HTTP_OK)
                .reason("Mocked")
                .headers(NO_HEADERS)
                .body(data));
    }

    public MockClient add(HttpMethod method, String url, Response.Builder response) {
        responses.put(new RequestKey(method, url), response);
        return this;
    }

    public Request verifyOne(HttpMethod method, String url) {
        return verifyTimes(method, url, 1).get(0);
    }

    public List<Request> verifyTimes(final HttpMethod method, final String url, final int times) {
        if (times < 0)
            throw new IllegalArgumentException("times must be a non negative number");

        if (times == 0) {
            verifyNever(method, url);
            return emptyList();
        }

        RequestKey key = new RequestKey(method, url);
        if (!requests.containsKey(key))
            throw new VerificationAssertionError("Wanted: '%s' but never invoked!", key);

        List<Request> result = requests.get(key);
        if (result.size() == times)
            return result;

        throw new VerificationAssertionError("Wanted: '%s' to be invoked: '%s' times but got: '%s'!",
                key, times, result.size());
    }

    public void verifyNever(HttpMethod method, String url) {
        RequestKey key = new RequestKey(method, url);
        if (requests.containsKey(key))
            throw new VerificationAssertionError("Do not wanted: '%s' but was invoked!", key);
    }

}
