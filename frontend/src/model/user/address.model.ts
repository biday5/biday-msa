// src/model/address.model.ts
export interface AddressModel {

    id: number;
    userId: string;

    streetAddress: string;
    detailAddress: string;

    zipcode: string;
    type: string;

    pick: boolean;
    email: string;

    address1: string;
    address2?: string;
}


// 주소 초기값 설정
export const initialAddress: AddressModel = {
    id: 0,
    userId: "",

    streetAddress: "",
    detailAddress: "",
    address1: '',
    address2: '',
    zipcode: '',
    type: '',
    pick: false,
    email: "",


};