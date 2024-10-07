//src/app/page.tsx
"use client";

import React, { useEffect, useState } from "react";
import { fetchAllProducts } from "@/service/product/product.api";
import {ProductModel} from "@/model/ProductModel";
import {getCookie} from "@/utils/cookie/cookie.api";

export default function PageHome() {
    const [products, setProducts] = useState<ProductModel[]>([]); // 상품 목록 상태 관리
    const [isLoading, setIsLoading] = useState<boolean>(true); // 로딩 상태 관리

    // 상품 데이터를 로드하는 함수
    const loadProducts = async () => {
        try {
            const productData = await fetchAllProducts(); // API 호출
            const productsArray = Object.values(productData); // 객체 값을 배열로 변환
            setProducts(productsArray); // 배열로 변환된 데이터 상태에 저장
            setIsLoading(false); // 로딩 상태 해제
        } catch (error) {
            console.error("상품 데이터를 가져오는 중 에러: ", error);
            setIsLoading(false); // 에러 발생 시 로딩 해제
        }
    };

    useEffect(() => {
        loadProducts(); // 컴포넌트가 처음 렌더링될 때 상품 데이터를 로드
    }, []);


    // products 배열이 유효한지 확인
    if (!products || products.length === 0) {
        return <div>상품이 없습니다.</div>;
    }

    // 카테고리별 상품 분류
    const categories: { [key: string]: ProductModel[] } = {
        상의: products.slice(0, 5),
        하의: products.slice(5, 10),
        신발: products.slice(10, 15),
        가방: products.slice(15, 20),
    };

    return (
        <main className="container mx-auto px-4 lg:px-8">
            {Object.keys(categories).map((category) => (
                <section key={category}>
                    <h2 className="text-xl font-bold my-4">{category}</h2>
                    <div className="grid grid-cols-5 gap-4">
                        {categories[category].map((product) => (
                            <div key={product.id} className="border p-4">
                                <div>{product.name}</div>
                                <div>{product.price}원</div>
                            </div>
                        ))}
                    </div>
                </section>
            ))}
        </main>
    );
}
