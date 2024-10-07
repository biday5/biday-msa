// src/api/account/account.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {AccountModel} from "@/model/user/account.model";

// 계좌 조회 (GET 요청)
const UserId = async (userId: string): Promise<AccountModel> => {
    const response = await strategy.GET(`${api.account}/${userId}`);
    return response;
};

// 계좌 등록 (POST 요청)
const save= async (accountData: AccountModel): Promise<AccountModel> => {
    const response = await strategy.POST(`${api.account}/save`, accountData);
    return response;
};

export const accountAPI = {
    UserId,
    save,
};
