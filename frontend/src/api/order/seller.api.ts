// src/api/sellerPayment/sellerPayment.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {SellerPaymentModel} from "@/model/order/sellerPayment.model";

// 모든 셀러 결제 조회 (GET 요청)
const findAll = async (): Promise<SellerPaymentModel[]> => {
    const response = await strategy.GET(`${api.seller}`);
    return response;
};

export const sellerPaymentAPI = {
    findAll,
};
