// src/api/wish/wish.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {WishModel} from "@/model/product/wish.model";

// 사용자별 위시 목록 조회 (GET 요청)
const findByUser = async (token: string): Promise<WishModel[]> => {
    const response = await strategy.GET(`${api.wish}/user`, {
        Authorization: token,
    });
    return response;
};

// 위시 토글 (GET 요청, 위시 생성/삭제)
const toggleWish = async (token: string, productId: number): Promise<string> => {
    const response = await strategy.GET(`${api.wish}`, {
        Authorization: token,
        productId: productId.toString(),
    });
    return response;
};

// 위시 삭제 (DELETE 요청)
const deleteWish = async (wishId: number): Promise<string> => {
    const response = await strategy.DELETE(`${api.wish}?id=${wishId}`);
    return response;
};

export const wishAPI = {
    findByUser,
    toggleWish,
    deleteWish,
};
