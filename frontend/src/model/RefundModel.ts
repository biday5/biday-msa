interface RefundModel {
    id?: number;
    payment: PaymentModel;
    amount: number;
    refundedAt: Date;
    status: PaymentStatus;
    reason: string;
    createdAt: Date;
    updatedAt: Date;
}