package shop.biday.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzService {

    private final Scheduler scheduler;

    public void createJob(Long auctionId, LocalDateTime endedAt) {
        log.info("SchedulerService createJob auctionId: {}, endedAt: {}", auctionId, endedAt);
        JobDetail jobDetail = buildJobDetail(QuartzJob.class, auctionId, endedAt);
        Trigger trigger = buildTrigger(auctionId, endedAt);

        try {
            if (scheduler.checkExists(jobDetail.getKey())) {
                log.info("이미 존재하는 Job: {}", jobDetail.getKey());
                scheduler.deleteJob(jobDetail.getKey());
                return;
            }

            log.info("새로운 Job 추가: {}", jobDetail.getKey());
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("SchedulerException e: ", e.getMessage());
        }
    }

    private Trigger buildTrigger(Long auctionId, LocalDateTime endedAt) {
        return TriggerBuilder.newTrigger()
                .withIdentity(StringUtils.joinWith("_", "AuctionEndsTrigger", auctionId))
                .withDescription("경매 종료 처리 Trigger")
                .startAt(Date.from(
                        endedAt.atZone(ZoneId.of("Asia/Seoul"))
                                .toInstant())
                )
                .build();
    }

    private JobDetail buildJobDetail(Class<? extends Job> job, Long auctionId, LocalDateTime endedAt) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("auctionId", auctionId);
        jobDataMap.put("endedAt", endedAt);
        jobDataMap.put("executeCount", 1);

        return JobBuilder.newJob(job)
                .withIdentity(StringUtils.joinWith("_", "AuctionEndsJob", auctionId))
                .withDescription("경매 종료 처리 Job")
                .usingJobData(jobDataMap)
                .build();
    }
}
