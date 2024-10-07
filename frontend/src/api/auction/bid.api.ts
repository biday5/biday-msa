// src/api/bid/bid.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {BidModel} from "@/model/auction/bid.model";



// 입찰 스트림 조회 (GET 요청 - SSE)
const streamBid = async (auctionId: number): Promise<BidModel[]> => {
    const response = await fetch(`${api.bid}/stream?auctionId=${auctionId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'text/event-stream',
        },
    });

    const reader = response.body?.getReader();
    const decoder = new TextDecoder("utf-8");

    if (reader) {
        let result: BidModel[] = [];

        while (true) {
            const { done, value } = await reader.read();
            if (done) break;

            const decodedValue = decoder.decode(value, { stream: true });

            try {
                const parsedData = JSON.parse(decodedValue) as BidModel;
                result.push(parsedData);
                console.log(parsedData);
            } catch (error) {
                console.error("Error parsing stream data", error);
            }
        }

        return result;
    }

    return [];
};


// 입찰 저장 (POST 요청)
const save = async (bidData: Partial<BidModel>): Promise<BidModel> => {
    const response = await strategy.POST(`${api.bid}`, bidData);
    return response;
};


const doOnClose = async (bidData: Partial<BidModel>): Promise<BidModel> => {
    const response = await strategy.POST(`${api.bid}`, bidData);
    return response;
    // 안에 내부 53번 임시로 사용을 한 소스 고쳐야한다.
};
export const bidAPI = {
    streamBid,
    save,
    doOnClose,
};