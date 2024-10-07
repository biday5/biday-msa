//src/model/notification.model.ts

import {UserModel} from "@/model/user/user.model";

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
    user: {} as UserModel,
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