import {ProductModel} from "@/model/product/product.model";
import {UserModel} from "@/model/UserModel";

export interface WishModel {
    id?: number;
    user: UserModel;
    product: ProductModel;
    status: boolean;
    createdAt: Date;
    updatedAt: Date;
}


export const initialWishModel: WishModel = {
    id: 0,
    user: {} as UserModel,
    product: {} as ProductModel,
    status: false,
    createdAt: new Date(),
    updatedAt: new Date(),
};