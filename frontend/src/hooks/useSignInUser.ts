// src/hooks/useSignUpUser.ts

import { useState } from "react";
import { signUpSchema } from '@/schema/userValidationSchema';
import { insertUser } from '@/service/user/signUp.api';
import { UserModel } from "@/model/UserModel";

const useSignUpUser = () => {
    const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error'>('idle');
    const [errorMessage, setErrorMessage] = useState<string>('');

    // 회원가입 처리 함수
    const handleSignUp = async (user : UserModel) => {
        setStatus('loading'); // 상태를 로딩 중으로 처리

        // Zod 스키마로 유효성 검사
        const validation = signUpSchema.safeParse(user);
        if (!validation.success) {
            const errorMessages = validation.error.issues.map((issue) => issue.message).join(', ');
            setErrorMessage(errorMessages); // 에러 메시지 설정
            setStatus('error');
            return false;
        }

        try {
            // insertUser 함수 호출 (API 요청)
            const response = await insertUser(user);

            // 회원가입 성공 여부 확인
            if (response.status === true) {
                setStatus('success');
                return true;
            } else {
                throw new Error('회원가입 실패');
            }
        } catch (error) {
            const err = error as Error;
            setStatus('error'); // 실패 시 상태를 error로 변경
            setErrorMessage(err.message || '회원가입 중 오류가 발생했습니다.');
            return false;
        }
    };

    return { status, handleSignUp, errorMessage }; // 상태와 함수 반환
};

export default useSignUpUser;
