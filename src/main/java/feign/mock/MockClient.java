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

import static com.google.common.io.ByteStreams.toByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;

public class MockClient implements Client {

    private static final Map<String, Collection<String>> NO_HEADERS = new HashMap<String, Collection<String>>();

    private static final int HTTP_NO_CONTENT = 204;

    private static final int HTTP_OK = 200;

    private final Map<RequestKey, Response> responses = new HashMap<>();

    @Override
    public Response execute(Request request, Options options) throws IOException {
        RequestKey key = new RequestKey(HttpMethod.valueOf(request.method()), request.url());

        if (responses.containsKey(key))
            return responses.get(key);

        return Response.create(404, "Not mocked", request.headers(), (byte[]) null);
    }

    public MockClient ok(HttpMethod method, String url, InputStream input) throws IOException {
        return ok(method, url, toByteArray(input));
    }

    public MockClient ok(HttpMethod method, String url, String text) {
        return ok(method, url, text.getBytes());
    }

    public MockClient noContent(HttpMethod method, String url) {
        return add(method, url, Response.create(HTTP_NO_CONTENT, "Mocked", NO_HEADERS, new byte[0]));
    }

    public MockClient ok(HttpMethod method, String url, byte[] data) {
        return add(method, url, Response.create(HTTP_OK, "Mocked", NO_HEADERS, data));
    }

    public MockClient add(HttpMethod method, String url, Response response) {
        responses.put(new RequestKey(method, url), response);
        return this;
    }

}
