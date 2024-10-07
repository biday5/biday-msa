import React from "react";
import Input from "@/shared/Input/Input";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import Link from "next/link";
import { RootState } from "@/lib/store";

export default function PageForgotPass({}){
  return (
    <div className="container mb-24 lg:mb-32">
      <header className="text-center max-w-2xl mx-auto - mb-14 sm:mb-16 lg:mb-20">
        <h2 className="mt-20 flex items-center text-3xl leading-[115%] md:text-5xl md:leading-[115%] font-semibold text-neutral-900 dark:text-neutral-100 justify-center">
          Forgot password
        </h2>
        <span className="block text-sm mt-4 text-neutral-700 sm:text-base dark:text-neutral-200">
          Welcome to our Community
        </span>
      </header>
{/*상ㅍㅁ은 해당 컴폰너트 자체는 클라잉ㄴ트 컴포넌ㅌ인데, 카드들 있잖아 ㅋ클라잉너틍ㄴ데 클라이언트 커뫂넌트에서 데이터를 가지고 올 숭 ㅓㅂㅅ다. 에이싱크 어웨잇 서버에서 ㅅ릇 윘으니 , ㅔㅍ이지자체를 서버로 맏늘어서, 인수로 프로젝트 객체로 넘겨줘야 한다.ㅍ ㅡ로젝트 객체르
이게 중요한게 서버 컴포넌트 클라잉ㄴ트 컴포넌트로 인ㅅ로 넘겾ㄹ 때 직렬화 / 클라*/}
      <div className="max-w-md mx-auto space-y-6">
        {/* FORM */}
        <form className="grid grid-cols-1 gap-6" action="#" method="post">
          <label className="block">
            <span className="text-neutral-800 dark:text-neutral-200">
              Email address
            </span>
            <Input
              type="email"
              placeholder="example@example.com"
              className="mt-1"
            />
          </label>
          <ButtonPrimary type="submit">Continue</ButtonPrimary>
        </form>

        {/* ==== */}
        <span className="block text-center text-neutral-700 dark:text-neutral-300">
          Go back for {` `}
          <Link href="/login" className="text-green-600">
            Sign in
          </Link>
          {` / `}
          <Link href="/signup" className="text-green-600">
            Sign up
          </Link>
        </span>
      </div>
    </div>
  );
};