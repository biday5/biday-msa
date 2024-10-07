export interface PaymentCancelModel {
    transactionKey: string;
    cancelReason: string;
    canceledAt: string;
    cancelAmount: number;
    cancelStatus: string;
}