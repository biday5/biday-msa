//src/model/account.model.ts

export interface AccountModel {
    id?: string;
    userId:number; // 단순작엄이다. useModel 타입으로 하지 말라고 하심.
    bankTranId:string;
    bankCode : string;
    bankName : string;
    accountNumber:string;
    accountName:string;
    bankRspCode:string;
    bankTranDate : Date;
}

export const initialAccountModel : AccountModel = {
    id: "",
    userId: 0,
    bankTranId:"",
    bankCode : "",
    bankName :"",
    accountNumber:"",
    accountName:"",
    bankRspCode:"",
    bankTranDate : new Date(),
}

/*export const aacountState: AccountModel = {
    id: 0, // 처음에는 ID가 없을 수 있으므로 undefined로 설정
};*/
