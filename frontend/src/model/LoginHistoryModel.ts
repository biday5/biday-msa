//src/model/LoginHistoryModel.ts

import {UserModel} from "@/model/UserModel";

export interface LoginHistoryModel {
    id?: number;
    user: UserModel;
    createdAt: Date;
}


export const initialLoginHistory: LoginHistoryModel = {
    id: 0,
    user: {
        id: 0,
        email: '',
        password: '',
    },
    createdAt: new Date(),
};