// src/hooks/useProductList.ts

import { useQuery } from '@tanstack/react-query';
import { getProductList } from '@/service/product/product.api';

export const useProductList = () => {
    return useQuery({
        queryKey: ['products'], // 캐시의 키
        queryFn: getProductList, // 상품 리스트를 가져오는 함수
        staleTime: 1000 * 60 * 5,  // 5분 동안 데이터를 캐싱
        retry: false,  // 실패 시 재시도하지 않음
    });
};

// 로그인 여부 상관 없이, 메인 페이지에서 상품 리스트를 가지고 오는 코드.

// useQuery를 통해 상품 리스트를 백엔드에서 가져오는 API를 호출을 해서,
// 리액트 쿼리를 통해 캐싱, 상태 관리를 자동으로 해주는 코드
// 메인 페이지 컴포넌트에서 간단히 사용할 수 있다.

// 재사용할 수 있게 리덕스 스토어에 저장도 함. useProductListWithRedux
// -->
// useProductList는 리액트 쿼리를 활용하여 상품 데이터를 백엔드에서 비동기적으로 가져오는 커스텀 훅이다.
// 이 훅은 로그인 여부와 관계없이 상품 데이터를 요청할 수 있으며, 서버로부터 받아온 데이터를 5분간 캐시하여 반복적인 API 호출을 최소화한다.
// 이를 통해 불필요한 서버 부하를 줄이고, 사용자 경험을 개선할 수 있다.

