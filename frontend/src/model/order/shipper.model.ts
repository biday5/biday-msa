import {PaymentModel} from "@/model/order/payment.model";

export interface ShipperModel {
    id?: number;
    paymentId: number;
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

export const initialShipperModel : ShipperModel = {
    id: 0,
    paymentId: 0,
    payment: {} as PaymentModel,
    carrier: "",
    trackingNumber: "",
    shipmentDate: new Date(),
    estimatedDeliveryDate: new Date(),
    deliveryDate: new Date(),
    status: "",
    deliveryAddress: "",
    createdAt: new Date(),
    updatedAt: new Date()
}

