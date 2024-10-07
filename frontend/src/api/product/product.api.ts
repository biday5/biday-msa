// src/api/product11/product11.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {ProductModel} from "@/model/product/product.model";

// 전체 상품 목록 불러오기 (GET 요청)
const findAll = async (): Promise<ProductModel[]> => {
    const response = await strategy.GET(`${api.product}/findAll`);
    return response;
};

// 필터를 이용한 상품 목록 불러오기 (GET 요청)
const searchByFilter = async (
    brandId?: number,
    categoryId?: number,
    keyword: string = '',
    color: string = '',
    order: string = '',
    lastItemId?: number
): Promise<ProductModel[]> => {
    const response = await strategy.GET(`${api.product}/findByFilter`, {
        ...(brandId && { brandId: brandId.toString() }),
        ...(categoryId && { categoryId: categoryId.toString() }),
        keyword,
        color,
        order,
        ...(lastItemId && { lastItemId: lastItemId.toString() }),
    });
    return response;
};

// 상품 상세 조회 (GET 요청)
const findById = async (id: number): Promise<ProductModel> => {
    const response = await strategy.GET(`${api.product}`, { id: id.toString() });
    return response;
};

// 상품 등록 (POST 요청)
const saveProduct = async (productData: Partial<ProductModel>): Promise<ProductModel> => {
    const response = await strategy.POST(`${api.product}`, productData);
    return response;
};

// 상품 수정 (PATCH 요청)
const updateProduct = async (productData: Partial<ProductModel>): Promise<ProductModel> => {
    const response = await strategy.PATCH(`${api.product}`, productData);
    return response;
};

// 상품 삭제 (DELETE 요청)
const deleteProduct = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.product}?id=${id}`);
};

export const productAPI = {
    findAll,
    searchByFilter,
    findById,
    saveProduct,
    updateProduct,
    deleteProduct,
};
