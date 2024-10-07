export interface PaymentCancelModel {
    transactionKey: string;
    cancelReason: string;
    canceledAt: string;
    cancelAmount: number;
    cancelStatus: string;
}

export const initialPaymentCancelModel : PaymentCancelModel = {
    transactionKey: "",
    cancelReason: "",
    canceledAt: "",
    cancelAmount: 0,
    cancelStatus: "",
}

