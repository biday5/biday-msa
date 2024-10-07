//src/lib/hooks/useUser.ts
import { useQuery } from '@tanstack/react-query';
import { findUserById } from '@/service/user/user.api';

export const useUser = (userId: string) => {
    return useQuery({
        queryKey: ['user', userId], // queryKey에 userId를 추가
        queryFn: () => findUserById(userId), // queryFn으로 findUserById 사용
        retry: false,  // 오류 시 재시도 하지 않음
        staleTime: 1000 * 60 * 5,  // 5분 동안 캐싱
    });
};

