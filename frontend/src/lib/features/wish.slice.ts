// src/lib/features/wish.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { WishModel } from '@/model/WishModel';
import { RootState } from '@/lib/store';

// 초기 상태: 빈 WishModel 배열
const initialState: WishModel[] = [];

// Wish Slice 생성
const wishSlice = createSlice({
    name: 'wish',
    initialState,
    reducers: {
        // 위시 추가
        addWish: (state, action: PayloadAction<WishModel>) => {
            state.push(action.payload); // 새로운 위시 추가
        },
        // 위시 삭제
        removeWish: (state, action: PayloadAction<number>) => {
            return state.filter(wish => wish.id !== action.payload); // ID로 위시 삭제
        },
        // 위시 업데이트
        updateWish: (state, action: PayloadAction<WishModel>) => {
            const index = state.findIndex(wish => wish.id === action.payload.id);
            if (index !== -1) {
                state[index] = action.payload; // 위시 업데이트
            }
        },
    },
});

// 상태 선택자 (Redux 상태에서 위시리스트 가져오기)
export const getWishes = (state: RootState) => state.wish;

// 액션 및 리듀서 내보내기
export const { addWish, removeWish, updateWish } = wishSlice.actions;
export default wishSlice.reducer;
