// src/api/faq/faq.api.ts
import { FaqModel } from "@/model/FaqModel";
import { api } from "../request";
import { strategy } from "../api.strategy";

// 모든 질문 조회
const findAll = async (): Promise<FaqModel[]> => {
    const response = await strategy.GET(`${api.faq}`);
    return response;
};

// 특정 질문 ID로 질문 조회
const findById = async (id: number): Promise<FaqModel> => {
    const response = await strategy.GET(`${api.faq}/${id}`);
    return response;
};
// 새로운 질문 등록
const addQuestion = async (questionData: Partial<FaqModel>): Promise<FaqModel> => {
    const response = await strategy.POST(`${api.faq}`, questionData);
    return response;
};

// 특정 질문 삭제
const deleteById = async (id: number): Promise<void> => {
    await strategy.DELETE(`${api.faq}/${id}`);
};

// 특정 질문 존재 여부 확인
const existsById = async (id: number): Promise<boolean> => {
    const response = await strategy.GET(`${api.faq}/exists/${id}`);
    return response;
};

export const faqAPI = {
    findAll,
    findById,
    addQuestion,
    deleteById,
    existsById,
};
