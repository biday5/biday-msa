// src/lib/features/category.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CategoryModel } from '@/model/CategoryModel';
import { RootState } from '@/lib/store';

// 초기 상태 정의
const initialState: CategoryModel[] = []; // 카테고리 정보를 배열로 관리

const categorySlice = createSlice({
    name: 'category',
    initialState, //initialState: 카테고리 정보를 저장할 배열로 설정했습니다.
    reducers: {
        // 카테고리 추가
        addCategory: (state, action: PayloadAction<CategoryModel>) => {
            state.push(action.payload);
        },
        // 카테고리 삭제
        removeCategory: (state, action: PayloadAction<number>) => {
            return state.filter(category => category.id !== action.payload);
        },
        // 카테고리 업데이트
        updateCategory: (state, action: PayloadAction<CategoryModel>) => {
            const index = state.findIndex(category => category.id === action.payload.id);
            if (index !== -1) {
                state[index] = action.payload;
            }
        },
    },
});

// 상태 선택자
export const getCategories = (state: RootState) => state.category;

// 액션 및 리듀서 내보내기
export const { addCategory, removeCategory, updateCategory } = categorySlice.actions;
export default categorySlice.reducer;
