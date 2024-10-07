//src/model/award.model.ts
import {AuctionModel} from "@/model/auction/auction.model";

export interface AwardModel {
    id: number;
    auction: AuctionModel;
    userId: string;
    bidedAt: Date;
    currentBid: number;
    count: number;
}

export const initialAward: AwardModel = {
    id: 0,
    auction: {} as AuctionModel,
    userId: '',
    bidedAt: new Date(),
    currentBid: 0,
    count: 1,
};