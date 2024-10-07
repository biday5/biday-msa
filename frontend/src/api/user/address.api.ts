// src/api/address/address.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {AddressModel} from "@/model/user/address.model";

// 주소 목록 조회 (GET 요청)
const findAllByUserId = async (userId: string, role: string, name: string): Promise<AddressModel[]> => {
    const response = await strategy.GET(`${api.address}/list`, {
        userId,
        role,
        name,
    });
    return response;
};

// 주소 선택 (PUT 요청)
const pickAddress = async (id: string): Promise<string> => {
    const response = await strategy.PUT(`${api.address}/pick`, { id });
    return response;
};

// 주소 수 카운트 (GET 요청)
const countAddresses = async (token: string): Promise<number> => {
    const response = await strategy.GET(`${api.address}/count`, {
        Authorization: token,
    });
    return response;
};

// 주소 삭제 (DELETE 요청)
const deleteAddressById = async (id: string): Promise<void> => {
    await strategy.DELETE(`${api.address}/deleteById/${id}`); // id를 경로 파라미터로 전달
};

// 주소 등록 (POST 요청)
const insertAddress = async (token: string, addressData: AddressModel): Promise<AddressModel> => {
    const response = await strategy.POST(`${api.address}/insert`, addressData, {
        Authorization: token,
    });
    return response;
};

export const addressAPI = {
    findAllByUserId,
    pickAddress,
    countAddresses,
    deleteAddressById,
    insertAddress,
};
