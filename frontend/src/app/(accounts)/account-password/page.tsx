"use client"
import React, { useState } from "react";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import Input from "@/shared/Input/Input";
import { changepass } from "@/service/user/user.api";
import {getUser} from "@/lib/features/user.slice";
import {useSelector} from "react-redux";
import {useChangePassword} from "@/hooks/useChangePassword"; // 비밀번호 변경 API 임포트

export default function AccountPass() {
  const user = useSelector(getUser); // Redux에서 유저 정보 가져오기
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const {changePassword} = useChangePassword();

  console.log("유저 정보 이메일 있는지 확인 : ", user);

  const handlePasswordChange = async () => {
    try {
      await changePassword(currentPassword, newPassword, confirmPassword);
      alert("비밀번호가 성공적으로 변경되었습니다.");
    } catch (error) {
      console.error(error);
      alert("비밀번호 변경에 실패했습니다.");
    }
  };

  return (
      <div className="space-y-10 sm:space-y-12">
        <h2 className="text-2xl sm:text-3xl font-semibold">비밀번호 변경</h2>
        <div className="max-w-xl space-y-6">
          <div>
            <label>현재 비밀번호</label>
            <Input
                type="password"
                className="mt-1.5"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
            />
          </div>
          <div>
            <label>새 비밀번호</label>
            <Input
                type="password"
                className="mt-1.5"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>
          <div>
            <label>새 비밀번호 확인</label>
            <Input
                type="password"
                className="mt-1.5"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>
          <div className="pt-2">
            <ButtonPrimary onClick={handlePasswordChange}>
              비밀번호 수정
            </ButtonPrimary>
          </div>
        </div>
      </div>
  );
}
