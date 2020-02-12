package com.basketbandit.rizumu.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduleHandler {
    private static final Logger log = LoggerFactory.getLogger(ScheduleHandler.class);
    private static final Set<ScheduledFuture<?>> tasks = new HashSet<>();
    private static final ScheduledExecutorService schedulerService = Executors.newScheduledThreadPool(1, Executors.defaultThreadFactory());

    /**
     * Registers a job that will be repeatedly executed at a fixed rate.
     * @param job {@link Job}
     */
    public static void registerJob(Job job) {
        log.info("recurring job registered: " + job.toString() + ", delay: " + job.getDelay() + job.getUnit() + ", period: " + job.getPeriod());
        tasks.add(schedulerService.scheduleAtFixedRate(job, job.getDelay(), job.getPeriod(), job.getUnit()));
    }

    /**
     * Registers a job to be executed a single time (unique).
     * @param job {@link Job}
     */
    public static void registerUniqueJob(Job job) {
        log.info("unique job registered: " + job.toString() + ", delay: " + job.getDelay() + job.getUnit());
        tasks.add(schedulerService.schedule(job, job.getDelay(), job.getUnit()));
    }
}