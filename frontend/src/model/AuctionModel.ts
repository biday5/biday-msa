import {ProductModel} from "@/model/ProductModel";
import {UserModel} from "@/model/UserModel";
import {BidModel} from "@/model/BidModel";
import {ImageModel} from "@/model/ImageModel";
import {AwardModel} from "@/model/AwardModel";

export interface AuctionModel {
    id?: number;
    userId: number;
    product: ProductModel;
    description: string;
    startingBid: number;
    currentBid: number;
    startedAt: Date;
    endedAt: Date;
    status: boolean;
    createdAt: Date;
    updatedAt: Date;
    award: AwardModel;
    size: string;
}

export interface AuctionDetailModel {
    id:number;
    user:string;
    description: string;
    currentBid: number;
    award: AwardModel;
    startedAt: Date;
    endedAt: Date;
    size: number;
    count: number;
    bidedAt: Date;
    startingBid: number;
    status: boolean;
    updatedAt: Date;
}