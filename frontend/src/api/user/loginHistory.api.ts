// src/api/loginHistory/loginHistory.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {LoginHistoryModel} from "@/model/user/loginHistory.model";

// 유저 로그인이력 조회 (GET 요청)
const findByUserId = async (userId: string): Promise<boolean> => {
    const response = await strategy.GET(`${api.loginHistory}/${userId}`);
    return await response.json(); // 서버가 Mono<Boolean>을 반환하므로 boolean으로 처리
};

// 유저 로그인이력 저장 (POST 요청)
const saveLoginHistory = async (loginHistoryData: LoginHistoryModel): Promise<LoginHistoryModel> => {
    const response = await strategy.POST(`${api.loginHistory}`, loginHistoryData);
    return await response.json(); // 서버가 Mono<LoginHistoryDocument>를 반환하므로 LoginHistoryModel로 처리
};

export const loginHistoryAPI = {
    findByUserId,
    saveLoginHistory,
};
