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

import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;

public class MockClientTest {

    interface GitHub {
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);
    }

    static class Contributor {
        String login;
        int contributions;
    }

    private GitHub github;

    @Before
    public void setup() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/fixtures/contributors.json");) {
            github = Feign.builder()
                    .decoder(new GsonDecoder())
                    .client(new MockClient().ok("mock:///repos/netflix/feign/contributors", input))
                    .target(GitHub.class, "mock://");
        }
    }

    @Test
    public void test() {
        List<Contributor> contributors = github.contributors("netflix", "feign");
        MatcherAssert.assertThat(contributors, hasSize(30));
    }

}
