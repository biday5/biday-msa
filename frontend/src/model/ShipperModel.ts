export interface ShipperModel {
    id?: number;
    payment: PaymentModel;
    carrier: string;
    trackingNumber: string;
    shipmentDate: Date;
    estimatedDeliveryDate: Date;
    deliveryDate: Date;
    status: string;
    deliveryAddress: string;
    createdAt: Date;
    updatedAt: Date;
}