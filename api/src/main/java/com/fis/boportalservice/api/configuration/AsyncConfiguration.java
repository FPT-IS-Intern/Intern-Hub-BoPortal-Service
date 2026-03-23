package com.fis.boportalservice.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class AsyncConfiguration {

  @Bean(name = "asyncTask")
  public Executor asyncTask(
      @Value("${threadPool.asyncTask.maxPoolSize}") int maxPoolSize,
      @Value("${threadPool.asyncTask.corePoolSize}") int corePoolSize,
      @Value("${threadPool.asyncTask.queueCapacity}") int queueCapacity) {
    log.info("event=ASYNC_TASK_EXECUTION_START");

    ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
    pool.setCorePoolSize(corePoolSize);
    pool.setMaxPoolSize(maxPoolSize);
    pool.setQueueCapacity(queueCapacity);
    pool.setThreadNamePrefix("async-task-");
    pool.setRejectedExecutionHandler((runnable, executor) -> log.error("task rejected"));
    pool.initialize();
    return pool;
  }
}
