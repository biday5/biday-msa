// src/api/image/image.api.ts
import { api } from "../request";
import { strategy } from "../api.strategy";
import {ImageModel} from "@/model/ftp/image.model";



// 이미지 불러오기
const getImageById = async (id: number): Promise<ImageModel> => {
    const response = await strategy.GET(`${api.image}/${id}`);
    return response;
};



// 단일 이미지 업로드
const uploadImage = async (imageData: Partial<ImageModel>): Promise<ImageModel> => {
    const response = await strategy.POST(`${api.image}`, imageData);
    return response;
};

// 이미지 업
const updateImages = async (): Promise<ImageModel[]> => {
    const response = await strategy.GET(`${api.image}`);
    return response; // response.json()이 이미 처리되어 반환됩니다.
};

const deleteImages = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.image}/${id}`);
};

const updateImages2 = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.image}/${id}`);
};

const deleteImages2 = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.image}/${id}`);
};
export const imageAPI = {
    getImageById,
    uploadImage,
    updateImages,
    deleteImages,
    updateImages2, // 오버로딩 컨트롤러 두개
    deleteImages2 // 오버로딩 컨트롤러 두개
};
