//src/model/UserModel.ts
import {AddressModel} from "@/model/AddressModel";

export interface UserModel {
    id: string;
    oauthName?: string;
    name?: string;
    email?: string;
    password?: string;
    phoneNum?: string;
    createdAt?: Date;
    updatedAt?: Date;
    status?: string;
    totalRating?: number;
    addresses?: AddressModel[];

    newPassword? : string;
}


// initialUser 설정
export const initialUser: UserModel = {
    id: '',
    oauthName: '',
    name: '',
    email: '',
    password: '',
    phoneNum: '',
    createdAt: new Date(),
    updatedAt: new Date(),
    status: '',
    totalRating: 0,
    addresses:[],
    newPassword:"",
};
