package shop.biday.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.biday.controller.BidController;
import shop.biday.model.document.BidDocument;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.entity.AwardEntity;
import shop.biday.service.AuctionService;
import shop.biday.service.AwardService;
import shop.biday.service.BidService;

@Slf4j
@Component
public class QuartzJob implements Job {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private BidController bidController;
    @Autowired
    private BidService bidService;
    @Autowired
    private AwardService awardService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        log.info("auctionEnds jobDataMap: {}", jobDataMap.toString());
        int executeCount = (int) jobDataMap.get("executeCount");
        jobDataMap.put("executeCount", ++executeCount);

        long auctionId = jobDataMap.getLong("auctionId");

        AuctionEntity auctionEntity = auctionService.updateState(auctionId);

        BidDocument findTopBid = bidService.findTopBidByAuctionId(auctionId).block();
        log.info("SchedulerJob findTopBid: {}", findTopBid);
        if (findTopBid == null) {
            log.info("입찰자가 없습니다.");
            bidController.sinkClose(auctionId);
            TriggerKey triggerKey = context.getTrigger().getKey();
            try {
                context.getScheduler().unscheduleJob(triggerKey);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            return;
        }

        Long count = bidService.countBidByAuctionIdAndUserId(auctionId, findTopBid.getUserId()).block();
        log.info("SchedulerJob count: {}", count);

        Boolean updatedAward = bidService.updateAward(auctionId).block();
        log.info("SchedulerJob updatedAward: {}", updatedAward);

        awardService.save(AwardEntity.builder()
                .auction(auctionEntity)
                .userId(findTopBid.getUserId())
                .bidedAt(findTopBid.getBidedAt())
                .currentBid(findTopBid.getCurrentBid())
                .count(count)
                .build());

        bidController.sinkClose(auctionId);
    }
}
