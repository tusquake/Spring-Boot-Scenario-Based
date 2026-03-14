package com.interview.debug.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * QUARTZ JOB: A more robust way to handle jobs.
 * If you use a Database-backed Quartz, these jobs survive server restarts
 * and can be distributed across a cluster.
 */
@Component
public class EmailJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("🏗️ QUARTZ JOB [EmailJob] -> Sending enterprise report at: {}", LocalDateTime.now());
        
        // In a real app, you'd pull data from JobDataMap
        String recipient = context.getJobDetail().getJobDataMap().getString("recipient");
        logger.info("🏗️ QUARTZ -> Recipient from JobDataMap: {}", recipient);
    }
}
