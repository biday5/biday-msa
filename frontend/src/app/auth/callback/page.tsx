// src/app/auth/callback/page.tsx
"use client"
import { useRouter } from 'next/router';
import {useEffect} from "react";

const NaverCallbackPage = () => {
    const router = useRouter();

    useEffect(() => {
        const { code, state } = router.query;

        if (code && state) {
            fetch(`${process.env.NEXT_PUBLIC_API_SERVER_URL}/oauth2/authorization/naver/callback`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ code, state }),
            })
                .then(response => response.json())
                .then(data => {
                    localStorage.setItem('accessToken', data.accessToken);
                    router.push('/');
                })
                .catch(error => console.error('Error fetching token:', error));
        }
    }, [router]);

    return <div>로그인 처리 중...</div>;
};

export default NaverCallbackPage;
