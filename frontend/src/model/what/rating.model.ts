//src/model/rating.model.ts

import {UserModel} from "@/model/user/user.model";

export interface RatingModel {
    id?: number;
    user: UserModel;
    rating: number;
    createdAt: Date;
}


export const initialRating: RatingModel = {
    id: 0,
    user: {} as UserModel,
    rating: 0,
    createdAt: new Date(),
};

// 디코딩, 클라이언트 연결 해서 테스트를 하는데 그 값이 넘거아야 하잖아.
//