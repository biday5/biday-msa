// src/api/category/category.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {CategoryModel} from "@/model/product/category.model";

const findAll = async (): Promise<CategoryModel[]> => {
    const response = await strategy.GET(`${api.category}`);
    return response;
};

const findById = async (id: number): Promise<CategoryModel> => {
    const response = await strategy.GET(`${api.category}/findById`, { id: String(id) });
    return response;
};

const create = async (categoryData: Partial<CategoryModel>): Promise<CategoryModel> => {
    const response = await strategy.POST(`${api.category}`, categoryData);
    return response;
};

const update = async (categoryData: Partial<CategoryModel>): Promise<CategoryModel> => {
    const response = await strategy.PATCH(`${api.category}`, categoryData);
    return response;
};

const delete_ = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.address}/${id}`);
};

export const categoryAPI = {
    findAll,
    findById,
    create,
    update,
    delete_,
};
