// src/lib/features/address.slice.ts

import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AddressModel, initialAddress } from '@/model/AddressModel';
import type { RootState } from '@/lib/store';

// AddressState 인터페이스 정의
interface AddressState {
    addresses: AddressModel[];
    selectedAddressId?: string;
}

const initialState: AddressState = {
    addresses: [initialAddress],  // 초기값으로 빈 주소 목록을 사용
    selectedAddressId: undefined,
};

const addressSlice = createSlice({
    name: 'address',
    initialState,
    reducers: {
        setAddresses: (state, action: PayloadAction<AddressModel[]>) => {
            state.addresses = action.payload;
        },
        addAddress: (state, action: PayloadAction<AddressModel>) => {
            state.addresses.push(action.payload);
        },
        removeAddress: (state, action: PayloadAction<string>) => {
            state.addresses = state.addresses.filter(address => address.userId !== action.payload);
        },
        pickAddress: (state, action: PayloadAction<string>) => {
            state.selectedAddressId = action.payload;
        },
    },
});

export const { setAddresses, addAddress, removeAddress, pickAddress } = addressSlice.actions;

export const selectAddresses = (state: RootState) => state.address.addresses;
export const selectPickedAddress = (state: RootState) => state.address.selectedAddressId;

export default addressSlice.reducer;
