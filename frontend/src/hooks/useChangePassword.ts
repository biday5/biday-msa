import { useState } from "react";
import { changepass } from "@/service/user/user.api";
import { getUser } from "@/lib/features/user.slice";
import { useSelector } from "react-redux";

export const useChangePassword = () => {
    const user = useSelector(getUser); // Redux에서 유저 정보 가져오기
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const changePassword = async (
        currentPassword: string,
        newPassword: string,
        confirmPassword: string
    ) => {
        if (newPassword !== confirmPassword) {
            setError("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return;
        }
        setIsLoading(true);
        setError(null);

        try {
            console.log("changePassword에서 newPassword 값 확인: ", newPassword); // 여기서 newPassword가 제대로 전달되는지 확인

            // Redux 상태에서 가져온 유저 정보 사용 (email 포함)
            const userModel = {
                id: user.user.id,
                email: user.user.email,
                password: currentPassword,  // 기존 비밀번호
                newPassword: newPassword,  // 새 비밀번호
            };

            // 비밀번호 변경 API 호출
            const response = await changepass(userModel);
            setIsLoading(false);

            return response;
        } catch (err) {
            setIsLoading(false);
            setError("비밀번호 변경에 실패했습니다.");
            console.error(err);
            throw err;
        }
    };

    return { changePassword, isLoading, error };
};
