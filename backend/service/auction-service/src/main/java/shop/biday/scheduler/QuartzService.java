package shop.biday.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
        log.info("QuartzService endedAt: {}", endedAt);

        // 1. endedAt이 이미 UTC 시간으로 전달되므로, UTC 시간대에 맞는 ZonedDateTime으로 변환
        ZonedDateTime utcZonedDateTime = endedAt.atZone(ZoneId.of("UTC"));
        log.info("QuartzService UTC ZonedDateTime: {}", utcZonedDateTime);

        // 2. UTC 시간에 9시간을 더하여 한국 시간(KST)으로 변환
        ZonedDateTime seoulZonedDateTime = utcZonedDateTime.plusHours(9);
        log.info("QuartzService Korea ZonedDateTime (KST): {}", seoulZonedDateTime);

        // 3. 한국 시간(KST)을 Instant로 변환
        Instant instant = seoulZonedDateTime.toInstant();
        log.info("QuartzService Korea Instant: {}", instant);

        // 4. Instant를 Date로 변환 (Date는 UTC 기준으로 저장되므로, 한국 시간으로 변환된 값을 밀리초로 반환)
        Date startedAt = Date.from(instant);
        log.info("QuartzService startedAt (KST): {}", startedAt);
        log.info("QuartzService startedAt millisecond: {}", startedAt.getTime());

        return TriggerBuilder.newTrigger()
                .withIdentity(StringUtils.joinWith("_", "AuctionEndsTrigger", auctionId))
                .withDescription("경매 종료 처리 Trigger")
                .startAt(startedAt)
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
