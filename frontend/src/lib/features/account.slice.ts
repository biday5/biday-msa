//src/lib/features/account.slice.ts
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "@/lib/store";
import {AccountModel, aacountState} from "@/model/AccountModel";



const accountSlice = createSlice({
    name: 'account',
    initialState: aacountState,
    reducers: {
        // 유저 ID 저장
        saveAccountId: (state, action: PayloadAction<number>) => {
            state.id = action.payload; // 유저 ID 설정
        },
        // 유저 ID 초기화 (로그아웃 시)
        clearAccountId: (state) => {
            state.id = undefined; // ID 초기화
        },
    },
});

// 상태 선택자
export const getAccountId = (state: RootState) => state.account.id;

// 액션 및 리듀서 내보내기
export const { saveAccountId, clearAccountId } = accountSlice.actions;
export default accountSlice.reducer;