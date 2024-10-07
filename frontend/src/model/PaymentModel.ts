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
    //cancels: PaymentCancelModel[];
    //card: PaymentCardModel;
    //easyPay: PaymentEasyPayModel;
    //country: string;
    //failure: PaymentFailureModel;
    code: string;
    message: string;
}