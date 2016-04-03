package feign.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.ByteStreams;

import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;

public class MockClient implements Client {

	private final Map<String, Response> responses = new HashMap<>();

	@Override
	public Response execute(Request request, Options options) throws IOException {
		String url = request.url();

		if (responses.containsKey(url))
			return responses.get(url);

		return Response.create(404, "Not mocked", request.headers(), (byte[]) null);
	}

	public MockClient add(String url, InputStream input) throws IOException {
		return add(url, ByteStreams.toByteArray(input));
	}

	public MockClient add(String url, String text) {
		return add(url, text.getBytes());
	}

	private MockClient add(String url, byte[] data) {
		return add(url, Response.create(200, "Not mocked", new HashMap<>(), data));
	}

	private MockClient add(String url, Response response) {
		responses.put(url, response);
		return this;
	}

}
