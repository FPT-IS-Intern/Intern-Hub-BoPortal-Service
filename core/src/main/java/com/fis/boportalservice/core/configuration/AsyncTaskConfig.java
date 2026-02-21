package com.fis.boportalservice.core.configuration;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
public class AsyncTaskConfig {

    @Value("${threadPool.asyncTask.maxPoolSize}")
    public int maxPoolSize;

    @Value("${threadPool.asyncTask.corePoolSize}")
    public int corePoolSize;

    @Value("${threadPool.asyncTask.queueCapacity}")
    public int queueCapacity;

    @Bean(name = "asyncTask")
    Executor asyncTask() {
        log.info("System runs asynchronous task");
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setQueueCapacity(queueCapacity);
        pool.setThreadNamePrefix("async-task-");
        pool.setRejectedExecutionHandler((runnable, threadPoolExecutor) -> log.error("task rejected"));
        pool.initialize();
        return pool;
    }
}
