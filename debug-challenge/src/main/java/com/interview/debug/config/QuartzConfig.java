package com.interview.debug.config;

import com.interview.debug.scheduler.EmailJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    /**
     * Define WHAT the job does.
     */
    @Bean
    public JobDetail emailJobDetail() {
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity("emailJob")
                .usingJobData("recipient", "admin@pizza.com")
                .storeDurably() // Keep it in DB even if no trigger is active
                .build();
    }

    /**
     * Define WHEN the job runs.
     */
    @Bean
    public Trigger emailJobTrigger() {
        // Run every 30 seconds
        return TriggerBuilder.newTrigger()
                .forJob(emailJobDetail())
                .withIdentity("emailTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?"))
                .build();
    }
}
