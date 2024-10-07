import {AuctionModel} from "@/model/AuctionModel";
import {api} from "../request";
import {strategy} from "../api.strategy";

// 경매 상세보기 (GET 요청)
const findById = async (id: number): Promise<AuctionModel> => {
    const response = await strategy.GET(`${api.auction}/findById`, {id: id.toString()});
    return response;
};

// 헤더 경매 목록 조회 (GET 요청)
const findBySize = async (sizeId?: number, order?: string, cursor?: number): Promise<AuctionModel[]> => {
    const response = await strategy.GET(`${api.auction}/findBySize`, {
        sizeId: sizeId?.toString() || '',  // undefined인 경우 빈 문자열로 처리
        order: order || '',  // undefined인 경우 빈 문자열로 처리
        cursor: cursor?.toString() || '',  // undefined인 경우 빈 문자열로 처리
    });
    return response;
};

// 상품 상세 경매 목록 조회 (GET 요청)
const findAllBySize = async (sizeId: number, order?: string): Promise<AuctionModel[]> => {
    const response = await strategy.GET(`${api.auction}/findAllBySize`, {
        sizeId: sizeId.toString(),
        order: order || '',  // undefined일 경우 빈 문자열로 처리
    });
    return response;
};

// 마이페이지 경매 목록 조회 (GET 요청)
const findByUser = async (userId: string, period: string, cursor?: number): Promise<AuctionModel[]> => {
    const response = await strategy.GET(`${api.auction}`, {
        userId,
        period,
        cursor: cursor?.toString() || '',  // undefined일 경우 빈 문자열로 처리
    });
    return response;
};

// 경매 등록 (POST 요청)
const save = async (auctionData: Partial<AuctionModel>): Promise<AuctionModel> => {
    const response = await strategy.POST(`${api.auction}`, auctionData);
    return response;
};

// 경매 수정 (PATCH 요청)
const update = async (auctionData: Partial<AuctionModel>): Promise<AuctionModel> => {
    const response = await strategy.PATCH(`${api.auction}`, auctionData);
    return response;
};

// 경매 삭제 (DELETE 요청)
const delete_ = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.auction}?id=${id}`);
};

export const auctionAPI = {
    findById,
    findBySize,
    findAllBySize,
    findByUser,
    update,
    save,
    delete_ // 키워드 딜리트라는 단억나 키워드여서 _ 언더바를 준거다. 다른 이름으로 아 자바 컨트롤러랑 맞추고 싶은데 에러가 나서 한거.
};
