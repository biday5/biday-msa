import {ProductModel} from "@/model/ProductModel";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "@/lib/store";

interface ProductsState {
    products: ProductModel[];
    loading: boolean;
}

const initialState: ProductsState = {
    products: [],
    loading: false,
}

const productsSlice = createSlice({
    name: 'products',
    initialState,
    reducers: {
        setProducts(state, action: PayloadAction<ProductModel[]>) {
            state.products = action.payload;
        },
        setLoading(state, action: PayloadAction<boolean>) {
            state.loading = action.payload;
        },
    }
})

export const getProducts = (state: RootState) => state.products.products;

export const {setProducts, setLoading} = productsSlice.actions;
export default productsSlice.reducer;