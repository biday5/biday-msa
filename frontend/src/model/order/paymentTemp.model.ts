export interface paymentTempModel  {
    orderId:string;
    awardId:string;
    amount:number;
}

export const initialPaymentTempModel : paymentTempModel = {
    orderId:"",
    awardId:"",
    amount:0,
}