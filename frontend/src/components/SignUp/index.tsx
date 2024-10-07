'use client'

import { useForm, SubmitHandler } from "react-hook-form";

// 폼 데이터의 타입 정의
interface IFormInput {
    email: string;
    phone: string;
}

const SignUp = () => {
    // useForm의 타입을 명시적으로 정의
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting }
    } = useForm<IFormInput>({
        mode: "onChange",
    });

    // 유효성 검사를 통과했을 때 실행될 함수
    const onValid: SubmitHandler<IFormInput> = (data) => {
        console.log("폼 데이터: ", data);
    };

    // 유효성 검사를 통과하지 못했을 때 실행될 함수
    const onInValid = (errors: any) => {
        console.log("유효성 검사 실패: ", errors);
    };

    return (
        <div>
            <form onSubmit={handleSubmit(onValid, onInValid)}>
                {/* 이메일 입력 */}
                <input
                    type="email"
                    placeholder="이메일을 입력해주세요."
                    {...register("email", {
                        required: "이메일은 필수 입력입니다.",
                        pattern: {
                            value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
                            message: "이메일 형식이 올바르지 않습니다."
                        }
                    })}
                />
                {errors.email && <span>{errors.email.message}</span>}

                {/* 핸드폰 번호 입력 */}
                <input
                    type="tel"
                    placeholder="핸드폰 번호를 입력해주세요."
                    {...register("phone", {
                        required: "핸드폰 번호는 필수 입력입니다.",
                        pattern: {
                            value: /^\d{3}-\d{4}-\d{4}$/,
                            message: "핸드폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"
                        }
                    })}
                />
                {errors.phone && <span>{errors.phone.message}</span>}

                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "제출 중..." : "회원가입완료"}
                </button>
            </form>
        </div>
    );
};

export default SignUp;
