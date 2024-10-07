import {UserModel} from "@/model/UserModel";
import {ProductModel} from "@/model/ProductModel";

export interface WishModel {
    id?: number;
    user: UserModel;
    product: ProductModel;
    status: boolean;
    createdAt: Date;
    updatedAt: Date;
}