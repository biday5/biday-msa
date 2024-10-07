// src/lib/features/product.slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ProductModel } from '@/model/ProductModel';

interface ProductState {
    products: ProductModel[];
}

const initialState: ProductState = {
    products: [],
};

const productSlice = createSlice({
    name: 'products',
    initialState,
    reducers: {
        setProducts: (state, action: PayloadAction<ProductModel[]>) => {
            state.products = action.payload;
        },
    },
});

export const { setProducts } = productSlice.actions;
export default productSlice.reducer;

// 상품 데이터를 리덕스에 저장을 하는 이유 :
// 리덕스는 전역 상태 관리를 담당하는 도구이다. 상품 데이터를 리덕스에 저장하면 다른 컴포넌트에서도 쉽게 상품 데이터를 접근할 수 있고,
// 특히 상품을 한 번만 가져오면, 그 데이터를 여러 곳에서 사용할 수 있게 되는 장점이 있다.
// 사용자가 페이지를 이동하더라도 상품 데이터를 다시 요청 하지 않아도 되고 리덕스에서 바로 데이터를 꺼내서 사용할 수 있다.
