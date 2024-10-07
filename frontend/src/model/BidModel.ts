export interface BidModel {
    /*id?: number;
    auction: AuctionModel;
    userId: number;
    bidedAt: Date;
    currentBid: Date;
    count: number;
    createdAt: Date;
    award: boolean;
    paymentTemp: PaymentTempModel;*/

    auctionId: number;
    userId: string;
    currentBid: number;
}

export interface BidStreamModel {
    auctionId: number;
    currentBid: number;
    award: boolean;
    count: number;
    bidedAt: Date;
}