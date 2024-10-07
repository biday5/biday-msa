//src/model/NotificationModel.ts
import {UserModel} from "@/model/UserModel";

interface NotificationModel {
    id: number;
    user: UserModel;
    type: NotificationTypeModel;
    message: string;
    status: boolean;
    createdAt: Date;
    updatedAt: Date;
}


export const initialNotification: NotificationModel = {
    id: 0,
    user: {
        id: 0,
        email: '',
        password: '',
    },
    type: {
        id: 0,
        name: '',
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    message: '',
    status: false,
    createdAt: new Date(),
    updatedAt: new Date(),
};