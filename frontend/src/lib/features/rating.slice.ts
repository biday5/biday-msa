// src/lib/features/rating.slice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { RatingModel } from '@/model/RatingModel';
import { RootState } from '@/lib/store';

// 비동기 API 요청을 위한 Thunk 생성 (유저 등급 정보 가져오기)
export const fetchRatings = createAsyncThunk('ratings/fetchRatings', async (userId: number) => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/ratings/${userId}`); // 서버에서 유저 등급 정보 가져오기
    const data = await response.json();
    return data as RatingModel[]; // 등급 정보를 배열로 반환
});

// 초기 상태 정의
interface RatingState {
    ratings: RatingModel[];
    status: 'idle' | 'loading' | 'succeeded' | 'failed';
    error: string | null;
}

const initialState: RatingState = {
    ratings: [],
    status: 'idle',
    error: null,
};

// Slice 생성
const ratingSlice = createSlice({
    name: 'rating',
    initialState,
    reducers: {
        // 등급 추가
        addRating: (state, action: PayloadAction<RatingModel>) => {
            state.ratings.push(action.payload);
        },
        // 등급 삭제
        removeRating: (state, action: PayloadAction<number>) => {
            state.ratings = state.ratings.filter(rating => rating.id !== action.payload);
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchRatings.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchRatings.fulfilled, (state, action: PayloadAction<RatingModel[]>) => {
                state.status = 'succeeded';
                state.ratings = action.payload; // 서버에서 가져온 등급 정보로 상태 업데이트
            })
            .addCase(fetchRatings.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message || 'Failed to fetch ratings';
            });
    },
});

// 상태 선택자
export const getRatings = (state: RootState) => state.rating.ratings;
export const getRatingStatus = (state: RootState) => state.rating.status;

// 액션 및 리듀서 내보내기
export const { addRating, removeRating } = ratingSlice.actions;
export default ratingSlice.reducer;
