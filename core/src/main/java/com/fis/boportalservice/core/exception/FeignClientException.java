package com.fis.boportalservice.core.exception;

import feign.FeignException;
import feign.Request;
import java.nio.ByteBuffer;
import java.util.*;

public class FeignClientException extends FeignException.FeignServerException {
    public FeignClientException(
            int status,
            String message,
            Request request,
            byte[] responseBody,
            Map<String, Collection<String>> headers) {
        super(status, message, request, responseBody, headers);
    }

    public FeignClientException(FeignException ex) {
        super(
                ex.status(),
                ex.getMessage(),
                ex.request(),
                toByteArray(ex.responseBody()),
                ex.responseHeaders());
    }

    private static byte[] toByteArray(java.util.Optional<ByteBuffer> optBuf) {
        if (optBuf.isPresent()) {
            ByteBuffer buf = optBuf.get();
            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);
            return bytes;
        }
        return new byte[0];
    }
}
