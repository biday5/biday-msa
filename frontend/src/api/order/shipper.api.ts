// src/api/shipper/shipper.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {ShipperModel} from "@/model/order/shipper.model";

// 모든 배송 목록 조회 (GET 요청)
const findAll = async (): Promise<ShipperModel[]> => {
    const response = await strategy.GET(`${api.shipper}`);
    return response;
};

// 특정 배송 상세보기 (GET 요청)
const findById = async (id: number): Promise<ShipperModel> => {
    const response = await strategy.GET(`${api.shipper}/findById`, { id: String(id) });
    return response;
};

// 배송 등록 (POST 요청)
const create = async (token: string, shipperData: Partial<ShipperModel>): Promise<ShipperModel> => {
    const response = await strategy.POST(`${api.shipper}`, shipperData, { Authorization: token });
    return response;
};

// 배송 수정 (PATCH 요청)
const update = async (shipperData: Partial<ShipperModel>): Promise<ShipperModel> => {
    const response = await strategy.PATCH(`${api.shipper}`, shipperData);
    return response;
};

// 배송 삭제 (DELETE 요청)
const delete_ = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.shipper}/${id}`);
};

export const shipperAPI = {
    findAll,
    findById,
    create,
    update,
    delete_,
};
