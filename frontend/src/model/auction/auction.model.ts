//src/model/auction.model.ts
import {AwardModel} from "@/model/auction/award.model";

export interface AuctionModel {
    id?: number;
    userId: number;
    description: string;
    startingBid: number;
    currentBid: number;
    startedAt: Date;
    endedAt: Date;
    status: boolean;
    createdAt: Date;
    updatedAt: Date;
    size:number,
    award:AwardModel,
}

export const initialAuction : AuctionModel = {
    id: 0,
    userId: 0,
    description: '',
    startingBid: 0,
    currentBid: 0,
    startedAt: new Date(),
    endedAt: new Date(),
    status: false,
    createdAt: new Date(),
    updatedAt: new Date(),
    size:0,
    award: {} as AwardModel,
}
//     award: {} as AwardModel, 여기서 이게 new 생성자이다. new AwardModel() x