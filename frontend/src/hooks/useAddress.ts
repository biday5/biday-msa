//src/hooks/useAddress.ts

import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { getAddressList, insertAddress, deleteAddress, pickAddress } from '@/service/address/address.api';
import { setAddresses, addAddress, removeAddress, pickAddress as pickAddressAction } from '@/lib/features/address.slice';
import { RootState } from '@/lib/store';
import {AddressModel} from "@/model/AddressModel";


export function useAddress(token: string) {
    const dispatch = useDispatch();
    const addresses = useSelector((state: RootState) => state.address.addresses);

    // 주소 목록 불러오기
    useEffect(() => {
        async function fetchAddresses() {
            try {
                const addressList = await getAddressList(token);
                dispatch(setAddresses(addressList));
            } catch (error) {
                console.error('주소를 불러오는 중 오류 발생', error);
            }
        }

        fetchAddresses();
    }, [dispatch, token]);

    // 새로운 주소 등록
    async function handleAddAddress(newAddress: AddressModel) {
        try {
            const addedAddress = await insertAddress(token, newAddress);
            dispatch(addAddress(addedAddress));
        } catch (error) {
            console.error('주소 등록 중 오류 발생', error);
        }
    }

    async function handlePickAddress(addressId: number) {
        try {
            await pickAddress(token, addressId); // addressId를 API에서 String으로 처리하게 됨
            // 주소 선택 완료 후 다른 로직
        } catch (error) {
            console.error('주소 선택 중 오류 발생', error);
        }
    }

// 예: 주소 삭제 핸들러
    async function handleRemoveAddress(addressId: number) {
        try {
            await deleteAddress(token, addressId); // addressId를 API에서 String으로 처리하게 됨
            // 주소 삭제 완료 후 다른 로직
        } catch (error) {
            console.error('주소 삭제 중 오류 발생', error);
        }
    }
}