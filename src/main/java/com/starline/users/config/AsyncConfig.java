package com.starline.users.config;

import io.opentelemetry.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("custom-async-");
        executor.setQueueCapacity(500);
        executor.initialize();
        return Context.taskWrapping(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.warn("Method: {}, Class: {} has thrown Uncaught exception in async task {}", method.getName(),
                        method.getDeclaringClass().getCanonicalName(),
                        ex.getMessage());
    }
}
