//src/model/RatingModel.ts
import {UserModel} from "@/model/UserModel";

export interface RatingModel {
    id?: number;
    user: UserModel;
    rating: number;
    createdAt: Date;
}


export const initialRating: RatingModel = {
    id: 0,
    user: {
        id: 0,
        email: '',
        password: '',
    },
    rating: 0,
    createdAt: new Date(),
};