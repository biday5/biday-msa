// src/app/auth/callback/NaverCallback.tsx
"use client";
import { useEffect } from 'react';
import { useRouter } from 'next/navigation'; // App Router에서 사용하는 useRouter

const NaverCallback = () => {
    const router = useRouter();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const authCode = params.get('code');
        const state = params.get('state');

        console.log("현재 URL 파라미터:", window.location.search);
        console.log("네이버로부터 받은 코드:", authCode);
        console.log("네이버로부터 받은 상태:", state);

        if (authCode && state) {
            const getToken = async () => {
                try {
                    const response = await fetch(`${process.env.NEXT_PUBLIC_API_SERVER_URL}/auth/callback`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({ code: authCode, state }) // 인증 코드와 상태 값 전송
                    });

                    const data = await response.json();
                    console.log("백엔드 응답 데이터:", data); // 서버로부터 받은 응답 데이터 확인

                    if (data.accessToken) {
                        // 받은 토큰을 localStorage에 저장
                        localStorage.setItem('accessToken', data.accessToken);
                        localStorage.setItem('refreshToken', data.refreshToken);
                        console.log("토큰이 저장되었습니다.");
                        // 로그인 성공 후 홈으로 리다이렉트
                        router.push('/');
                    } else {
                        console.error('토큰을 받는 데 실패했습니다.', data);
                    }
                } catch (error) {
                    console.error('토큰 요청 중 오류가 발생했습니다:', error);
                }
            };

            getToken();
        } else {
            console.error("네이버로부터 받은 코드나 상태가 없습니다.");
        }
    }, [router]);

    return <div>로그인 처리 중...</div>;
};

export default NaverCallback;
