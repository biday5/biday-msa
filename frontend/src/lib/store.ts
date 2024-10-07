//src/lib/store.ts
import {configureStore} from '@reduxjs/toolkit';
import userSlice from '@/lib/features/user.slice';
import productSlice from "@/lib/features/product.slice";
import wishSlice from "@/lib/features/wish.slice";
import accountSlice from "@/lib/features/account.slice";
import addressSlice from "@/lib/features/address.slice";
import brandSlice from "@/lib/features/brand.slice";
import categorySlice from "@/lib/features/category.slice";
import faqSlice from "@/lib/features/faq.slice";
import loginHistorySlice from "@/lib/features/loginHistory.slice";
import ratingSlice from "@/lib/features/rating.slice";
import { persistReducer, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage'; // 기본적으로 localStorage 사용
import { combineReducers } from 'redux';

// redux-persist 설정
const persistConfig = {
    key: 'root',
    storage,      // localStorage 사용
    whitelist: ['user'],  // user만 persist에 저장
};

// 루트 리듀서 정의 (combineReducers 사용)
const rootReducer = combineReducers({
    user: userSlice,
    product: productSlice,
    wish: wishSlice,
    account: accountSlice,
    address: addressSlice,
    brand: brandSlice,
    category: categorySlice,
    faq: faqSlice,
    loginHistory: loginHistorySlice,
    rating: ratingSlice,
});


// persistReducer로 루트 리듀서를 감싸줌
const persistedReducer = persistReducer(persistConfig, rootReducer);

// 스토어 설정 함수
export const makeStore = () => {
    return configureStore({
        reducer: persistedReducer,
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware({
                serializableCheck: false,  // 직렬화 경고 무시
                ignoredActions: ["persist/PERSIST", "persist/REHYDRATE"],
            }),
        devTools: process.env.NODE_ENV !== 'production',  // DevTools 활성화 여부 설정 (production 환경에서는 비활성화)
    });
};

export const store = makeStore();
export const persistor = typeof window !== 'undefined' ? persistStore(store) : null;
export type AppStore = ReturnType<typeof makeStore>;
export type RootState = ReturnType<AppStore['getState']>;
export type AppDispatch = AppStore['dispatch'];


// 리덕스에서 /슬라이스를 사용하는 이유는 유저 슬라이스를 만들었잖아.
// 그 다음에 프로덕트 슬라이스 만들었잖아.
// 슬라이스를 쓰는 이유가 페이지에서만 쓰는거잖아.
// 완성이 안됐잖아. 꼴랑 두개 넣고, 완성이 안된 상태에서 에러가 나오잖아.
// 에러 나왔다고 하니, 유즈 클라이언트 하고, 에러 고치고 또 똘리고,
// 이미 에러가 나올거 다 아니깐,
// 슬라이스는 어떤 페이지에서 슬라이스를 쓰는거잖아.
// 유즈 클라이언트는 당연한거고 슬라이스는

