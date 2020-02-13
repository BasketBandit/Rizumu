package com.basketbandit.rizumu.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ScheduleHandler {
    private static final Logger log = LoggerFactory.getLogger(ScheduleHandler.class);

    private static final ScheduledExecutorService schedulerService = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<Job, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<Job> pausedTasks = new CopyOnWriteArrayList<>();

    /**
     * Registers a job that will be repeatedly executed at a fixed rate.
     * @param job {@link Job}
     */
    public static void registerJob(Job job) {
        log.info("recurring job registered: " + job.toString() + ", delay: " + job.getDelay() + job.getUnit() + ", period: " + job.getPeriod());
        tasks.put(job, schedulerService.scheduleAtFixedRate(job, job.getDelay(), job.getPeriod(), job.getUnit()));
    }

    /**
     * Registers a job to be executed a single time (unique).
     * @param job {@link Job}
     */
    public static void registerUniqueJob(Job job) {
        log.info("unique job registered: " + job.toString() + ", delay: " + job.getDelay() + job.getUnit());
        tasks.put(job, schedulerService.schedule(job, job.getDelay(), job.getUnit()));
    }

    /**
     * Pauses (effectively) execution of the ScheduledExecutorService
     */
    public static void pauseExecution() {
        for(Job job : tasks.keySet()) {
            ScheduledFuture<?> task = tasks.get(job);
            if(task.getDelay(TimeUnit.MILLISECONDS) > -1 && !task.isCancelled()) {
                job.setDelay(task.getDelay(TimeUnit.MILLISECONDS));
                pausedTasks.add(job);
            }
            task.cancel(true);
        }
        tasks.clear();
    }

    /**
     * Resumes execution of the ScheduledExecutorService
     */
    public static void resumeExecution() {
        pausedTasks.forEach(ScheduleHandler::registerUniqueJob);
        pausedTasks.clear();
    }

    /**
     * Cancels execution of the ScheduledExecutorService
     */
    public static void cancelExecution() {
        tasks.values().forEach(scheduledFuture -> scheduledFuture.cancel(true));
        tasks.clear();
        pausedTasks.clear();
    }
}