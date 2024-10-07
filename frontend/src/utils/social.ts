// src/utils/social.ts

import {signin} from "next-auth/core/routes"; // next-auth에서 제공을 해주는 siginIn 함수를 사용해야함.

export const handleSocialSignin = async (provider: Provider) => signin(provider);

// signIn 함수의 인자는 Provider가 된다.
//   -예를들어, 카카오 로그인의 경우 signIn('kakao')와 같이 작성합니다.