package com.fis.boportalservice.core.util;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TracingUtils {

    public static <T> Supplier<T> wrapWithTracing(Context context, Supplier<T> supplier) {

        Map<String, String> mdc = MDC.getCopyOfContextMap();

        return () -> {
            try (Scope scope = context.makeCurrent()) {
                if (mdc != null) MDC.setContextMap(mdc);
                return supplier.get();
            } finally {
                MDC.clear();
            }
        };
    }
}
