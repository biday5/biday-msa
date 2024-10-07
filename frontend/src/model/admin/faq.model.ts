import {UserModel} from "@/model/user/user.model";

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
    user: {} as UserModel,
    title: '',
    content: '',
    createdAt: new Date(),
    updatedAt: new Date(),
};