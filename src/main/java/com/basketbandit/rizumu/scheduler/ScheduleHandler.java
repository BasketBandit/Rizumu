package com.basketbandit.rizumu.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduleHandler {
    private static final Set<ScheduledFuture<?>> tasks = new HashSet<>();
    private static final ScheduledExecutorService schedulerService = Executors.newScheduledThreadPool(1);

    /**
     * Registers a job that will be repeatedly executed at a fixed rate.
     * @param job {@link Job}
     */
    public static void registerJob(Job job) {
        tasks.add(schedulerService.scheduleAtFixedRate(job, job.getDelay(), job.getPeriod(), job.getUnit()));
    }

    /**
     * Registers a job to be executed a single time (unique).
     * @param job {@link Job}
     * @return {@link ScheduledFuture}
     */
    public static ScheduledFuture registerUniqueJob(Job job) {
        return schedulerService.schedule(job, job.getDelay(), job.getUnit());
    }

}