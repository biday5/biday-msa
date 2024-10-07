"use client"
//src/app/signup/page.tsx

import React, {FC, useState} from "react";
import facebookSvg from "@/images/Facebook.svg";
import twitterSvg from "@/images/Twitter.svg";
import googleSvg from "@/images/Google.svg";
import Input from "@/shared/Input/Input";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import Image from "next/image";
import Link from "next/link";
import useSignUpUser from "@/hooks/useSignInUser";
import {insertUser} from "@/service/user/user.api";
import {UserModel} from "@/model/UserModel";
import {signUpSchema} from "@/schema/userValidationSchema";
import {FormControl, FormLabel} from "@chakra-ui/react";
import {router} from "next/client";


const loginSocials = [
  {
    name: "Continue with Facebook",
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

export default function PageSignUp(){
  const { status, handleSignUp, errorMessage } = useSignUpUser(); // 송준한 커스텀 훅
  const [formData, setFormData] = useState<Partial<UserModel>>({
    name: '',
    email: '',
    password: '',
    phoneNum: '',
    //zipcode: '',
    //streetAddress: '',
    //detailAddress: '',
    //addressType: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>{
    const { name, value } = e.target;
    setFormData({...formData, [name]: value});
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Use the existing formData state directly, no need to re-declare it
    const validation = signUpSchema.safeParse(formData);
    console.log("폼 데이터 : 핸들체인지 블록 ", JSON.stringify(formData));


    if (validation.success) {
      const validData: UserModel = validation.data;
      // validation.data는 이제 UserModel과 동일한 구조
    } else {
      const errorMessages = validation.error.issues.map(issue => issue.message).join(", ");
      alert(errorMessages); // 에러 처리
      return;
    }

    // Proceed to submit the form data
    const result = await insertUser(validation.data);
    if (result.status) {
      alert("회원가입 성공");
    } else {
      alert("회원가입 실패");
    }
  };

  return (
      <div className={`nc-PageSignUp`} data-nc-id="PageSignUp">
        <div className="container mb-24 lg:mb-32">
          <h2 className="my-20 flex items-center text-3xl leading-[115%] md:text-5xl md:leading-[115%] font-semibold text-neutral-900 dark:text-neutral-100 justify-center">
            Signup
          </h2>
          <div className="max-w-md mx-auto space-y-6">
            {/* 소셜 로그인 버튼 */}
            <div className="grid gap-3">
              {loginSocials.map((item, index) => (
                  <a
                      key={index}
                      href={item.href}
                      className="flex w-full rounded-lg bg-primary-50 dark:bg-neutral-800 px-4 py-3 transform transition-transform sm:px-6 hover:translate-y-[-2px]"
                  >
                    <Image sizes="40px" className="flex-shrink-0" src={item.icon} alt={item.name} />
                    <h3 className="flex-grow text-center text-sm font-medium text-neutral-700 dark:text-neutral-300 sm:text-sm">
                      {item.name}
                    </h3>
                  </a>
              ))}
            </div>

            {/* OR */}
            <div className="relative text-center">
            <span className="relative z-10 inline-block px-4 font-medium text-sm bg-white dark:text-neutral-400 dark:bg-neutral-900">
              OR
            </span>
              <div className="absolute left-0 w-full top-1/2 transform -translate-y-1/2 border border-neutral-100 dark:border-neutral-800"></div>
            </div>

            {/* 회원가입 폼 */}
            <form className="grid grid-cols-1 gap-6" onSubmit={handleSubmit}>
              {/* 이름 입력 필드 */}
              <label className="block">
                <span className="text-neutral-800 dark:text-neutral-200">이름</span>
                <Input
                    type="text"
                    placeholder="이름 입력"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    className="mt-1"
                />
                <span className="text-sm text-gray-500">이름은 6글자 이하로 입력해주세요.</span>
              </label>

              {/* 이메일 입력 필드 */}
              <label className="block">
                <span className="text-neutral-800 dark:text-neutral-200">Email address</span>
                <Input
                    type="email"
                    placeholder="example@example.com"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    className="mt-1"
                />
              </label>

              {/* 비밀번호 입력 필드 */}
              <label className="block">
                <span className="flex justify-between items-center text-neutral-800 dark:text-neutral-200">Password</span>
                <Input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    className="mt-1"
                    placeholder="비밀번호 입력"
                />
                <span className="text-sm text-gray-500">
                비밀번호는 최소 11글자 이상이어야 하며, 대문자와 특수문자를 포함해야 합니다.
              </span>
              </label>

              <FormControl mb={4}>
                <FormLabel htmlFor="phoneNum">전화번호</FormLabel>
                <Input
                    type="text"
                    id="phoneNum"
                    name="phoneNum"
                    value={formData.phoneNum}
                    onChange={handleChange}
                    required
                />
              </FormControl>

              {/* 제출 버튼 */}
              <ButtonPrimary type="submit">Continue</ButtonPrimary>
            </form>

            {/* 이미 계정이 있는 경우 로그인 링크 */}
            <span className="block text-center text-neutral-700 dark:text-neutral-300">
            Already have an account?{' '}
              <Link className="text-green-600" href="/login">
              Sign in
            </Link>
          </span>
          </div>
        </div>
      </div>
  );
};