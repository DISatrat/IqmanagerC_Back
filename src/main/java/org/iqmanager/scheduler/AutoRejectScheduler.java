package org.iqmanager.scheduler;

import org.iqmanager.service.orderElementService.OrderElementService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class AutoRejectScheduler extends QuartzJobBean {

    private final OrderElementService orderElementService;

    public AutoRejectScheduler(OrderElementService orderElementService) {
        this.orderElementService = orderElementService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        jobExecutionContext.getJobDetail().getJobDataMap().get("");
    }
}
