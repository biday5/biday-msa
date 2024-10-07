// src/lib/features/loginHistory.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LoginHistoryModel } from '@/model/LoginHistoryModel';
import { RootState } from '@/lib/store';

// 초기 상태 정의
const initialState: LoginHistoryModel[] = []; // 로그인 기록 배열로 관리

const loginHistorySlice = createSlice({
    name: 'loginHistory',
    initialState,
    reducers: {
        // 로그인 기록 추가
        addLoginHistory: (state, action: PayloadAction<LoginHistoryModel>) => {
            state.push(action.payload);
        },
        // 로그인 기록 삭제
        removeLoginHistory: (state, action: PayloadAction<number>) => {
            return state.filter(login => login.id !== action.payload);
        },
    },
});

// 상태 선택자
export const getLoginHistory = (state: RootState) => state.loginHistory;

// 액션 및 리듀서 내보내기
export const { addLoginHistory, removeLoginHistory } = loginHistorySlice.actions;
export default loginHistorySlice.reducer;
