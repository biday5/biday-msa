//src/model/AwardModel.ts
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
    auction: {
        id: 0,
        title: '',
        description: '',
        startingBid: 0,
        closingDate: new Date(),
    },
    userId: '',
    bidedAt: new Date(),
    currentBid: 0,
    count: 1,
};