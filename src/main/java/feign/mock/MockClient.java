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

import java.io.ByteArrayOutputStream;
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

    private static final HashMap<String, Collection<String>> NO_HEADERS = new HashMap<String, Collection<String>>();

    private static final int HTTP_NO_CONTENT = 204;

    private static final int HTTP_OK = 200;

    private final Map<String, Response> responses = new HashMap<>();

    @Override
    public Response execute(Request request, Options options) throws IOException {
        String url = request.url();

        if (responses.containsKey(url))
            return responses.get(url);

        return Response.create(404, "Not mocked", request.headers(), (byte[]) null);
    }

    public MockClient ok(String url, InputStream input) throws IOException {
        return ok(url, toByte(input));
    }

    private byte[] toByte(InputStream input) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = input.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }

    public MockClient ok(String url, String text) {
        return ok(url, text.getBytes());
    }

    public MockClient noContent(String url) {
        return add(url, Response.create(HTTP_NO_CONTENT, "Mocked", NO_HEADERS, new byte[0]));
    }

    public MockClient ok(String url, byte[] data) {
        return add(url, Response.create(HTTP_OK, "Mocked", NO_HEADERS, data));
    }

    public MockClient add(String url, Response response) {
        responses.put(url, response);
        return this;
    }

}
