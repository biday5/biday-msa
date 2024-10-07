// src/lib/features/brand.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '@/lib/store';

// 초기 상태 정의
const initialState: BrandModel[] = []; // 브랜드 리스트를 배열로 관리

const brandSlice = createSlice({
    name: 'brand',
    initialState,
    reducers: {
        // 브랜드 추가
        addBrand: (state, action: PayloadAction<BrandModel>) => {
            state.push(action.payload);
        },
        // 브랜드 삭제
        removeBrand: (state, action: PayloadAction<number>) => {
            return state.filter(brand => brand.id !== action.payload);
        },
        // 브랜드 업데이트
        updateBrand: (state, action: PayloadAction<BrandModel>) => {
            const index = state.findIndex(brand => brand.id === action.payload.id);
            if (index !== -1) {
                state[index] = action.payload;
            }
        },
    },
});

// 상태 선택자
export const getBrands = (state: RootState) => state.brand;

// 액션 및 리듀서 내보내기
export const { addBrand, removeBrand, updateBrand } = brandSlice.actions;
export default brandSlice.reducer;
