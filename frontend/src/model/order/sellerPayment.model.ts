import {PaymentModel} from "@/model/order/payment.model";
import exp from "node:constants";
import PaymentMethod from "@/app/checkout/PaymentMethod";
/*

interface SellerPaymentModel {
    id?: number;
    payment: PaymentModel;
    transactionId: number;
    amount: number;
    refundedAt: Date;
    status: PaymentStatus;
    reason: string;
    createdAt: Date;
    updatedAt: Date;
}*/


export interface SellerPaymentModel {
    id?:number;
    paymentId:number;
    transactionId:string;
    totalAmount:number;
    fee:number;
    netAmount:number;
    //paymentMethod:PaymentMethod;
    //status:paymentStatus;
    // 강사님한테 물어보기, 백엔드 디티오에는 메서드를 타입으로 받는다고.
    paymentDate:Date;
    createdAt:Date;
    updatedAt:Date;
}

export const initialSellerPaymentModel : SellerPaymentModel = {
    id:0,
    paymentId:0,
    transactionId:"",
    totalAmount:0,
    fee:0,
    netAmount:0,
    //paymentMethod: {} as PaymentMethod(),
    //status: {} as paymentStatus(),
    paymentDate:new Date(),
    createdAt:new Date(),
    updatedAt:new Date(),
}