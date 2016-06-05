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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RequestKeyTest {

    @Test
    public void checkHashes() {
        RequestKey request1 = new RequestKey(HttpMethod.GET, "a");
        RequestKey request2 = new RequestKey(HttpMethod.GET, "b");

        assertThat(request1.hashCode(), equalTo(HttpMethod.GET.hashCode() + (7 * 31)));
        assertThat(request1.hashCode(), equalTo(request2.hashCode()));
        assertThat(request1, not(equalTo(request2)));
    }

    @Test
    public void equalObject() {
        RequestKey request1 = new RequestKey(HttpMethod.GET, "a");
        assertThat(request1, not(equalTo(new Object())));
    }

    @Test
    public void equalNull() {
        RequestKey request1 = new RequestKey(HttpMethod.GET, "a");
        assertThat(request1, not(equalTo(null)));
    }

    @Test
    public void equalPost() {
        RequestKey request1 = new RequestKey(HttpMethod.GET, "a");
        RequestKey request2 = new RequestKey(HttpMethod.POST, "a");

        assertThat(request1.hashCode(), not(equalTo(request2.hashCode())));
        assertThat(request1, not(equalTo(request2)));
    }

    @Test
    public void equalSelf() {
        RequestKey request1 = new RequestKey(HttpMethod.GET, "a");

        assertThat(request1.hashCode(), equalTo(request1.hashCode()));
        assertThat(request1, equalTo(request1));
    }

}
