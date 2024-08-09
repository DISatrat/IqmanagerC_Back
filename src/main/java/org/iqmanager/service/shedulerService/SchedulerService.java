package org.iqmanager.service.shedulerService;

import lombok.extern.slf4j.Slf4j;
import org.iqmanager.models.SchedulerJobInfo;
import org.iqmanager.repository.SchedulerRepository;
import org.iqmanager.scheduler.AutoRejectScheduler;
import org.iqmanager.scheduler.JobScheduleCreator;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Transactional
@Service
public class SchedulerService {

    private final Scheduler scheduler;

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final SchedulerRepository schedulerRepository;

    private final ApplicationContext context;

    private final JobScheduleCreator scheduleCreator;

    public SchedulerService(Scheduler scheduler, SchedulerFactoryBean schedulerFactoryBean, SchedulerRepository schedulerRepository, ApplicationContext context, JobScheduleCreator scheduleCreator) {
        this.scheduler = scheduler;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.schedulerRepository = schedulerRepository;
        this.context = context;
        this.scheduleCreator = scheduleCreator;
    }


    /** Создать задание */
    public void saveOrUpdate(SchedulerJobInfo scheduleJob) throws Exception {
        if (scheduleJob.getCronExpression().length() > 0) {
            if (Objects.equals(scheduleJob.getJobClass(), "AutoResponseScheduler")) {
                scheduleJob.setJobClass(AutoRejectScheduler.class.getName());
            }
            scheduleJob.setCronJob(true);
        } else {
            if (Objects.equals(scheduleJob.getJobClass(), "AutoResponseScheduler")) {
                scheduleJob.setJobClass(AutoRejectScheduler.class.getName());

            }
            scheduleJob.setCronJob(false);
            scheduleJob.setRepeatTime((long) 0);
        }
        if (StringUtils.isEmpty(scheduleJob.getId())) {
            scheduleNewJob(scheduleJob);
        } else {
            updateScheduleJob(scheduleJob);
        }
        scheduleJob.setDesc("i am job number " + scheduleJob.getId());
        scheduleJob.setInterfaceName("interface_" + scheduleJob.getId());
    }


    /** Новое задание */
    private void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob((Class) Class.forName(jobInfo.getJobClass()))
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob(
                        (Class) Class.forName(jobInfo.getJobClass()), false, context,
                        jobInfo.getJobName(), jobInfo.getJobGroup());

                Trigger trigger;
                if (jobInfo.getCronJob()) {
                    trigger = scheduleCreator.createCronTrigger(
                            jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getCronExpression(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {
                    trigger = scheduleCreator.createSimpleTrigger(
                            jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getRepeatTime(),

                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }
                scheduler.scheduleJob(jobDetail, trigger);
                jobInfo.setJobStatus("SCHEDULED");
                schedulerRepository.save(jobInfo);
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /** Обновить существующее */
    private void updateScheduleJob(SchedulerJobInfo jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {

            newTrigger = scheduleCreator.createCronTrigger(
                    jobInfo.getJobName(),
                    new Date(),
                    jobInfo.getCronExpression(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {

            newTrigger = scheduleCreator.createSimpleTrigger(
                    jobInfo.getJobName(),
                    new Date(),
                    jobInfo.getRepeatTime(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setJobStatus("EDITED & SCHEDULED");
            schedulerRepository.save(jobInfo);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /** Запустить задачу, не дожидаясь срабатывания тригера */
    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("SCHEDULED & STARTED");
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled and started now.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    /** Поставить на паузу выполнение задания */
    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("PAUSED");
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " paused.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    /** Возобновитьб задание */
    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("RESUMED");
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " resumed.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

     /** Удалить задание */
    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            schedulerRepository.delete(getJobInfo);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

}

