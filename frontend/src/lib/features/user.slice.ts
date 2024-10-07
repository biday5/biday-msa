//src/lib/features/user.slice.ts

import {createSlice, PayloadAction} from '@reduxjs/toolkit'
import type {RootState} from '@/lib/store'
import {initialUser, UserModel} from '@/model/UserModel';

export interface UserState {
    user: UserModel;
    token: string | null;
}

const initialState: UserState = {
    user: initialUser,
    token:null,
};

// saveUser 메서드(액션)는 유저 정보와 토큰을 한 번에 저장을 한다.
const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        saveUser: (state, action: PayloadAction<{ user: UserModel, token: string }>) => {
            state.user = action.payload.user;  // 유저 정보 저장
            state.token = action.payload.token;  // 토큰 저장
        },
        clearUser: (state) => {
            state.user = initialUser;  // 유저 정보 초기화
            state.token = null;  // 토큰 초기화
        },
    },
});

export const getUser = (state: RootState) => state.user;
export const {saveUser, clearUser} = userSlice.actions;
export const getToken = (state: RootState) => state.user.token;
export default userSlice.reducer;


// 액션이 메서드 기능이고, 스테이트가 속성이다.

// 유즈 슬라이스 여기 있잖아. 셋 유저 클리어 유저 만들었잔항.
// 셋은 세이브 라고 하고 / 세이브 프로덕트 / 이름은 단수형,
// 페이지는 시나리오상으로 이미 결정 되어 있으니 유즈 클라이언트, 익스포트 디폴트 하고 /
// 슬라이스 다 해놓기
//

//작은 액션느 리듀서라고 한다. 메서드 기능이다.
