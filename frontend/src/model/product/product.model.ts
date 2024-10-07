import {SizeModel} from "@/model/product/size.model";

export interface ProductModel {
    id: number;
    brand: string;
    category: string;
    name: string;
    subName?: string;
    productCode: string;
    price: number;
    color: string;
    description: string;
    createdAt: Date;
    updatedAt: Date;
    wishes:number;
    size: SizeModel[];
}
export type ProductDictionary = { [key: string]: ProductModel; };

export const initialProduct: ProductModel = {
    id: 0,
    brand: '',
    category: '',
    name: '',
    productCode: "",
    price: 0,
    color: '',
    description: '',
    createdAt: new Date(),
    updatedAt: new Date(),
    wishes:0,
    size: {} as SizeModel[],
};