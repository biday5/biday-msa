export interface PaymentCardModel {
    issueName: string;
    issueCode: string;
    installmentPlanMonths: number;
    approveNo: string;
    cardType: string;
    isInterestFree: boolean;
}

export const initialPaymentCardModel : PaymentCardModel = {
    issueName: "",
    issueCode: "",
    installmentPlanMonths: 0,
    approveNo: "",
    cardType: "",
    isInterestFree: false,
}