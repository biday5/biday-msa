export interface PaymentFailureModel {
    code: string;
    message: string;
}

export const initialPaymentFailureModel: PaymentFailureModel = {
    code: "",
    message:"",
}