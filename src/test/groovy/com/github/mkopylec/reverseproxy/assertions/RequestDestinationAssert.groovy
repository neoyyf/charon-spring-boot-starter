package com.github.mkopylec.reverseproxy.assertions

import com.github.tomakehurst.wiremock.client.RequestPatternBuilder
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

import static com.github.tomakehurst.wiremock.client.RequestPatternBuilder.allRequests
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.headRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.optionsRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.traceRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.PATCH
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpMethod.TRACE

class RequestDestinationAssert {

    private List<WireMockRule> actual

    protected RequestDestinationAssert(WireMockRule... actual) {
        assert actual != null
        this.actual = actual
    }

    RequestDestinationAssert withMethodAndUri(HttpMethod method, String uri) {
        verify {
            it.verify(requestedFor(method, urlPathEqualTo(uri)))
        }
        return this
    }

    RequestDestinationAssert withHeaders(HttpHeaders headers) {
        verify {
            def matcher = allRequests()
            headers.each {
                k, v -> matcher = matcher.withHeader(k, equalTo(v as String))
            }
            it.verify(matcher)
        }
        return this
    }

    RequestDestinationAssert withBody(String body) {
        verify {
            it.verify(allRequests().withRequestBody(equalTo(body)))
        }
        return this
    }

    private void verify(Closure verification) {
        def error = ''
        def matchesCount = actual.count {
            try {
                verification(it)
                return true
            } catch (VerificationException ex) {
                error += "$ex.message\n\n"
                return false
            }
        }
        if (matchesCount != 1) {
            throw new VerificationException(error)
        }
    }

    private static RequestPatternBuilder requestedFor(HttpMethod method, UrlMatchingStrategy urlMatchingStrategy) {
        switch (method) {
            case DELETE:
                return deleteRequestedFor(urlMatchingStrategy)
            case POST:
                return postRequestedFor(urlMatchingStrategy)
            case GET:
                return getRequestedFor(urlMatchingStrategy)
            case PUT:
                return putRequestedFor(urlMatchingStrategy)
            case OPTIONS:
                return optionsRequestedFor(urlMatchingStrategy)
            case TRACE:
                return traceRequestedFor(urlMatchingStrategy)
            case HEAD:
                return headRequestedFor(urlMatchingStrategy)
            case PATCH:
                return patchRequestedFor(urlMatchingStrategy)
            default:
                throw new RuntimeException("Invalid HTTP method: $method")
        }
    }
}
