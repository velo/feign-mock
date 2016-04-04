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

public class RequestKey {

    private final HttpMethod method;

    private final String url;

    public RequestKey(HttpMethod method, String url) {
        super();
        this.method = method;
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + method.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequestKey other = (RequestKey) obj;
        if (method != other.method)
            return false;
        if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Request [" + method + ": " + url + "]";
    }

}
