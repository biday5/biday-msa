// src/hooks/useTokenManager.ts

import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';

export const useTokenManager = () => {
    const router = useRouter();

    // accessToken으로 API 요청
    const fetchUsers = async () => {
        try {
            const accessToken = Cookies.get('accessToken');
            if (!accessToken) {
                throw new Error('Access token is missing');
            }

            const response = await fetch('http://localhost:8080/users/UsersList', {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                    "Content-Type": "application/json",
                },
                credentials: 'include', // 쿠키 자동 전송
            });

            if (response.status === 401) {
                // accessToken 만료 시 refreshToken을 사용해 새로 발급받음
                await handleReissueToken();
                fetchUsers(); // 토큰 재발급 후 다시 요청
            } else if (!response.ok) {
                throw new Error('Failed to fetch users');
            }

            const data = await response.json();
            console.log(data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    // refreshToken으로 accessToken 재발급
    const handleReissueToken = async () => {
        try {
            const response = await fetch("http://localhost:8080/reissue", {
                method: "POST",
                credentials: 'include',
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                const data = await response.json();
                Cookies.set('accessToken', data.accessToken, { expires: 1, sameSite: 'strict' });
                console.log('Token reissued successfully!');
            } else {
                console.error('Failed to reissue token');
            }
        } catch (error) {
            console.error('Error reissuing token:', error);
        }
    };

    // 로그아웃 처리
    const handleLogout = async () => {
        try {
            const response = await fetch("http://localhost:8080/logout", {
                method: "POST",
                credentials: 'include',
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                Cookies.remove('accessToken');
                console.log('Logged out successfully');
                router.push('/login');
            } else {
                console.error('Failed to log out');
            }
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return {
        fetchUsers,
        handleLogout,
        handleReissueToken,
    };
};
