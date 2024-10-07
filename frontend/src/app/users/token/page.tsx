"use client";

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';

export default function Home() {
    const router = useRouter();

    // accessToken으로 API 요청
    const fetchUsers = async () => {
        try {
            const accessToken = Cookies.get('accessToken'); // accessToken 가져오기
            if (!accessToken) {
                throw new Error('Access token is missing');
            }

            const response = await fetch('http://211.188.54.218:8080/users/UsersList', {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${accessToken}`, // accessToken 사용
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
                credentials: 'include',  // refreshToken이 HttpOnly 쿠키로 자동 전송됨
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
                credentials: 'include',  // refreshToken이 자동 전송됨
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                Cookies.remove('accessToken'); // accessToken 삭제
                console.log('Logged out successfully');
                router.push('/login');
            } else {
                console.error('Failed to log out');
            }
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return (
        <div>
            <button onClick={fetchUsers}>Fetch Users</button>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}
