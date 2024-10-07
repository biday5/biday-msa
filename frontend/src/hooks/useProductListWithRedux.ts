//src/hooks/useProductListWithRedux.ts
import { useQuery } from '@tanstack/react-query';
import { useDispatch } from 'react-redux';
import { getProductList } from '@/service/product/product.api';
import { setProducts } from '@/lib/features/product.slice';
import { useEffect } from 'react';

export const useProductListWithRedux = () => {
    const dispatch = useDispatch();

    const { data, isSuccess } = useQuery({
        queryKey: ['products'],
        queryFn: getProductList,
        staleTime: 1000 * 60 * 5,
        retry: false,
    });

    useEffect(() => {
        if (isSuccess && data) {
            dispatch(setProducts(data)); // 성공 시 리덕스에 저장
        }
    }, [isSuccess, data, dispatch]);

    return { data };
};
