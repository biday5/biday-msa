// src/api/brand/brand.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {BrandModel} from "@/model/product/brand.model";

// 브랜드 목록 불러오기 (GET 요청)
const findAll = async (): Promise<BrandModel[]> => {
    const response = await strategy.GET(`${api.brand}`);
    return response;
};

// 브랜드 상세보기 (GET 요청)
const findById = async (id: number): Promise<BrandModel> => {
    const response = await strategy.GET(`${api.brand}/findById`, { id: id.toString() });
    return response;
};

// 브랜드 등록 (POST 요청)
const create = async (brandData: Partial<BrandModel>): Promise<BrandModel> => {
    const response = await strategy.POST(`${api.brand}`, brandData);
    return response;
};

// 브랜드 수정 (PATCH 요청)
const update = async (brandData: Partial<BrandModel>): Promise<BrandModel> => {
    const response = await strategy.PATCH(`${api.brand}`, brandData);
    return response;
};

// 브랜드 삭제 (DELETE 요청)
const delete_ = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.brand}?id=${id}`);
};

export const brandAPI = {
    findAll,
    findById,
    create,
    update,
    delete_,
};
