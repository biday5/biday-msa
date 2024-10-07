// src/lib/features/faq.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { FaqModel } from '@/model/FaqModel';
import { RootState } from '@/lib/store';

// 초기 상태 정의
const initialState: FaqModel[] = []; // FAQ 정보를 배열로 관리

const faqSlice = createSlice({
    name: 'faq',
    initialState,
    reducers: {
        // FAQ 추가
        addFaq: (state, action: PayloadAction<FaqModel>) => {
            state.push(action.payload);
        },
        // FAQ 삭제
        removeFaq: (state, action: PayloadAction<number>) => {
            return state.filter(faq => faq.id !== action.payload);
        },
        // FAQ 업데이트
        updateFaq: (state, action: PayloadAction<FaqModel>) => {
            const index = state.findIndex(faq => faq.id === action.payload.id);
            if (index !== -1) {
                state[index] = action.payload;
            }
        },
    },
});

// 상태 선택자
export const getFaqs = (state: RootState) => state.faq;

// 액션 및 리듀서 내보내기
export const { addFaq, removeFaq, updateFaq } = faqSlice.actions;
export default faqSlice.reducer;
