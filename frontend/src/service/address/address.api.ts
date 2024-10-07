// src/service/address/address.api.ts

import { AddressModel } from '@/model/AddressModel';

const baseUrl = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/addresses`;

// 모든 주소 목록 불러오기 (토큰을 사용해서 해당 유저의 주소를 가져옴)
export async function getAddressList(token: string): Promise<AddressModel[]> {
    const response = await fetch(`${baseUrl}/list`, {
        method: 'GET',
        headers: {
            Authorization: token,
        },
    });
    if (!response.ok) {
        throw new Error('주소를 불러오는 중 오류 발생');
    }
    return response.json();
}

// 주소 등록하기
export async function insertAddress(token: string, address: AddressModel): Promise<AddressModel> {
    const response = await fetch(`${baseUrl}/insert`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: token,
        },
        body: JSON.stringify(address),
    });

    if (!response.ok) {
        throw new Error('주소 등록 중 오류 발생');
    }
    return response.json();
}

// 특정 주소 선택하기
export async function pickAddress(token: string, addressId: number): Promise<string> {
    const response = await fetch(`${baseUrl}/pick?id=${String(addressId)}`, {
        method: 'PUT',
        headers: {
            Authorization: token,
        },
    });

    if (!response.ok) {
        throw new Error('주소 선택 중 오류 발생');
    }
    return response.json();
}

// 특정 주소 삭제하기
export async function deleteAddress(token: string, addressId: number): Promise<boolean> {
    const response = await fetch(`${baseUrl}/deleteById?id=${String(addressId)}`, {
        method: 'DELETE',
        headers: {
            Authorization: token,
        },
    });

    if (!response.ok) {
        throw new Error('주소 삭제 중 오류 발생');
    }
    return true;
}

