package shop.biday.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class QuartzJobListener implements JobListener {

    private static final int MAX_RETRIES = 5;

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("JOB 수행 되기 전: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.warn("JOB 실행되기 전에 거부됨: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (!ObjectUtils.isEmpty(jobException)) {
            JobKey jobKey = context.getJobDetail().getKey();
            int executeCount = context.getMergedJobDataMap().getInt("executeCount");
            log.warn("Job {} 실패. 재시도 {}/{}", jobKey, executeCount + 1, MAX_RETRIES);

            try {
                if (executeCount < MAX_RETRIES) {
                    context.getMergedJobDataMap().put("executeCount", ++executeCount);
                    context.getScheduler().triggerJob(jobKey, context.getJobDetail().getJobDataMap());
                } else {
                    TriggerKey triggerKey = context.getTrigger().getKey();
                    context.getScheduler().unscheduleJob(triggerKey);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("Job 재시도 실패: ", e.getMessage());
            }
        } else {
            log.info("JOB 수행 완료 후: {}", context.getJobDetail().getKey());
        }
    }
}
