export interface PaymentEasyPayModel {
    provider: string;
    amount: number;
    discountAmount: number;
}

export const initialPaymentEasyPayModel: PaymentEasyPayModel = {
    provider: "",
    amount: 0,
    discountAmount: 0,
}