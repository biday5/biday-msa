//src/model/AccountModel.ts
export interface AccountModel {
    id?: number;
    userId: string;
    bankCode: string;
    bankName: string;
    accountName: string;
    accountNum: string;
    bankRspCode: string;
    bankTranDate: Date;
    bankRspMessage: string;
    createdAt: Date;
    updatedAt: Date;
}

export const aacountState: AccountModel = {
    id: 0, // 처음에는 ID가 없을 수 있으므로 undefined로 설정
};