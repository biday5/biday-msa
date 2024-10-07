//src/app/login/page.tsx
"use client";

import React, {useState, FormEvent} from 'react';
import facebookSvg from "@/images/Facebook.svg";
import twitterSvg from "@/images/Twitter.svg";
import googleSvg from "@/images/Google.svg";
import Input from "@/shared/Input/Input";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import Image from "next/image";
import Link from "next/link";
import {useLogin} from "@/hooks/useLogin";
import btnG_official from "@/images/btnG_official.png";
import {signIn} from "next-auth/react";

// 소셜 로그인 버튼 데이터
const loginSocials = [
    {
        name: "Continue with Naver",
        href: `${process.env.NEXT_PUBLIC_API_SERVER_URL}/oauth2/authorization/naver`,
        icon: btnG_official,
    },
    {
        name: "Continue with Facebook src/app/login/page.tsx",
        href: "#",
        icon: facebookSvg,
    },
    {
        name: "Continue with Twitter",
        href: "#",
        icon: twitterSvg,
    },
    {
        name: "Continue with Google",
        href: "#",
        icon: googleSvg,
    },
];

export default function PageLogin() {
    // 상태 관리
    const [email, setEmail] = useState<string>('');  // 사용자 입력은 이메일로 받음
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const {login} = useLogin();

    const handleLaverSigIn = () =>{
        signIn('naver',{
            callbackUrl: '/'
        })
    }

    // 입력 값 변경 처리 함수
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        if (name === 'email') {
            setEmail(value);
        } else if (name === 'password') {
            setPassword(value);
        }
    };


    // 로그인 폼 제출 처리 함수
    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        try {
            // 로그인 로직 실행
            await login(email, password);
        } catch (err) {
            setError("Login failed. Please check your email or password.");  // 에러 상태 업데이트
            console.log("실패다.", err)
        }
    };


    return (
        <div className={`nc-PageLogin`} data-nc-id="PageLogin">
            <div className="container mb-24 lg:mb-32">
                <h2 className="my-20 flex items-center text-3xl leading-[115%] md:text-5xl md:leading-[115%] font-semibold text-neutral-900 dark:text-neutral-100 justify-center">
                    Login
                </h2>
                <button onClick={handleLaverSigIn}>네이버 로그인 버튼</button>

                <div className="max-w-md mx-auto space-y-6">
                    {/* 소셜 로그인 버튼 */}
                    <div className="grid gap-3">
                        {loginSocials.map((item, index) => (
                            <a
                                key={index}
                                href={item.href}
                                className="flex w-full rounded-lg bg-primary-50 dark:bg-neutral-700 px-4 py-3 transform transition-transform sm:px-6 hover:translate-y-[-2px]"

                            >
                                <Image
                                    className="flex-shrink-0"
                                    src={item.icon}
                                    alt={item.name}
                                    width={24} // 너비 조정
                                    height={24} // 높이 조정
                                    sizes="40px"
                                />
                                <h3 className="flex-grow text-center text-sm font-medium text-neutral-700 dark:text-neutral-300 sm:text-sm">
                                    {item.name}
                                </h3>
                            </a>
                        ))}
                    </div>

                    {/* OR */}
                    <div className="relative text-center">
            <span
                className="relative z-10 inline-block px-4 font-medium text-sm bg-white dark:text-neutral-400 dark:bg-neutral-900">
              OR
            </span>
                        <div
                            className="absolute left-0 w-full top-1/2 transform -translate-y-1/2 border border-neutral-100 dark:border-neutral-800"></div>
                    </div>

                    {/* 로그인 폼 */}
                    <form className="grid grid-cols-1 gap-6" onSubmit={handleSubmit}>
                        <label className="block">
              <span className="text-neutral-800 dark:text-neutral-200">
                Email address
              </span>
                            <Input
                                type="email"
                                name="email"
                                value={email}
                                onChange={handleChange}
                                placeholder="example@example.com"
                                className="mt-1"
                            />
                        </label>

                        <label className="block">
              <span className="flex justify-between items-center text-neutral-800 dark:text-neutral-200">
                Password
                <Link href="/forgot-pass" className="text-sm text-green-600">
                  Forgot password?
                </Link>
              </span>
                            <Input
                                type="password"
                                name="password"
                                value={password}
                                onChange={handleChange}
                                className="mt-1"
                            />
                        </label>

                        <ButtonPrimary type="submit">Continue</ButtonPrimary>
                    </form>

                    {/* 에러 메시지 */}
                    {error && <p className="text-red-500">{error}</p>}

                    <span className="block text-center text-neutral-700 dark:text-neutral-300">
            New user? {` `}
                        <Link className="text-green-600" href="/signup">
              Create an account

            </Link>
          </span>
                </div>
            </div>
        </div>
    );
};