// src/model/size.model.ts
export interface SizeModel {
    id: number; // 사이즈 ID
    sizeProduct: string; // 사이즈가 등록될 상품 이름
    size: string; // 사이즈 (예: S, M, L 등)
    createdAt: Date; // 생성일 (ISO8601 날짜 문자열)
    updatedAt: Date; // 수정일 (ISO8601 날짜 문자열)
}

// 기본 초기값 설정
export const initialSize: SizeModel = {
    id: 0,
    sizeProduct: '',
    size: '',
    createdAt: new Date(),
    updatedAt:  new Date(),
};