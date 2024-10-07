//src/model/bid.model.ts
import {AuctionModel} from "@/model/auction/auction.model";

export interface BidModel {
    id?: number;
    auction: AuctionModel;
    userId: number;
    bidedAt: Date;
    currentBid: Date;
    count: number;
    createdAt: Date;
    award: boolean;
    //paymentTemp: PaymentTempModel;
}

