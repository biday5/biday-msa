import {PaymentModel} from "@/model/order/payment.model";

export interface RefundModel {
    id?: number;
    //송준한
    paymentId:number;
    payment: PaymentModel;
    amount: number;
    refundedAt: Date;
    status: PaymentStatus;
    reason: string;
    createdAt: Date;
    updatedAt: Date;
}

export const initialRefundModel : RefundModel = {
    id: 0,
    paymentId:0,
    payment: {} as PaymentModel,
    amount: 0,
    refundedAt: new Date(),
    status: {} as PaymentStatus,
    reason: "",
    createdAt: new Date(),
    updatedAt: new Date(),
}