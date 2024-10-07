// src/model/AddressModel.ts
export interface AddressModel {
    address1: string;
    address2?: string;
    zipcode: string;
    type: string;
    pick: boolean;
    userId: string;
}


// 주소 초기값 설정
export const initialAddress: AddressModel = {
    address1: '',
    address2: '',
    zipcode: '',
    type: 'home', // 'home', 'work', 'other' 등으로 설정할 수 있습니다.
    pick: false,
    userId: '',  // 유저 ID는 실제로 사용될 때 입력됩니다.
};