// src/api/size/size.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {SizeModel} from "@/model/product/size.model";

// 사이즈 전체 목록 조회
const findAll = async (): Promise<SizeModel[]> => {
    const response = await strategy.GET(`${api.size}`);
    return response;
};

// 특정 상품 ID로 사이즈 조회
const findAllByProductId = async (productId: number): Promise<SizeModel[]> => {
    const response = await strategy.GET(`${api.size}/findByProduct`, { id: String(productId) });
    return response;
};

/*// 특정 사이즈 ID로 상세 조회
const findById = async (id: number): Promise<SizeModel> => {
    const response = await strategy.GET(`${api.size}/findById`, { id: String(id) });
    return response;
//1006 백엔드에 없음.
};*/

// 사이즈 등록
const create = async (sizeData: Partial<SizeModel>): Promise<SizeModel> => {
    const response = await strategy.POST(`${api.size}`, sizeData);
    return response;
};

// 사이즈 수정
const update = async (sizeData: Partial<SizeModel>): Promise<SizeModel> => {
    const response = await strategy.PATCH(`${api.size}`, sizeData);
    return response;
};

// 사이즈 삭제
const delete_ = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.size}/${id}`);
};

export const sizeAPI = {
    findAll,
    findAllByProductId,
    create,
    update,
    delete_,
};
