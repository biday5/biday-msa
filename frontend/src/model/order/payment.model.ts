//src/model/payment.model.ts
import {PaymentCancelModel} from "@/model/order/paymentCancel.model";
import {PaymentCardModel} from "@/model/order/paymentCard.model";
import {PaymentEasyPayModel} from "@/model/order/paymentEasyPay.model";
import {PaymentFailureModel} from "@/model/order/paymentFailure.model";

export interface PaymentModel {
    paymentKey: string;
    type: string;
    orderId: string;
    currency: string;
    method: string;
    totalAmount: number;
    balanceAmount: number;
    status: string;
    requestedAt: string;
    approvedAt: string;
    suppliedAmount: number;
    vat: number;

    cancels: PaymentCancelModel[];
    card: PaymentCardModel;
    easyPay: PaymentEasyPayModel;
    country: string;
    failure: PaymentFailureModel;
    code: string;
    message: string;
}

export const initialPaymentModel : PaymentModel = {
    paymentKey: "",
    type: "",
    orderId:  "",
    currency: "",
    method:  "",
    totalAmount:0,
    balanceAmount:0,
    status:  "",
    requestedAt:  "",
    approvedAt: "",
    suppliedAmount: 0,
    vat:0,

    cancels: {} as PaymentCancelModel[],
    card: {} as PaymentCardModel,
    easyPay: {} as PaymentEasyPayModel,
    country: "",
    failure: {} as PaymentFailureModel,
    code: "",
    message:  "",
}