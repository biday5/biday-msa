import {UserModel} from "@/model/UserModel";

export interface FaqModel {
    id: number;
    user: UserModel;
    title: string;
    content: string;
    createdAt: Date;
    updatedAt: Date;
}

export const initialFaq: FaqModel = {
    id: 0,
    user: {
        id: undefined ,
        email: '',
        password: '',
    },
    title: '',
    content: '',
    createdAt: new Date(),
    updatedAt: new Date(),
};