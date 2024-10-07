/*
//src/service/user/signUp.api.ts
import { UserModel } from "@/model/UserModel";

export async function insertUser(user: UserModel): Promise<any> {
    try {
        // Prepare the body for the request
        const body = {
            name: user.name,
            email: user.email,
            password: user.password,
            phoneNum: user.phoneNum,
            zipcode: user.zipcode,
            streetAddress: user.streetAddress,
            detailAddress: user.detailAddress,
            type: user.addressType,
        };

        console.log(body,"asdf")
        console.log(typeof user , "유저")
        console.log("유저 에이파이 ",user)

        const response = await fetch('http://localhost:8080/api/users/join', {
            method: 'POST',
            headers: {
                // 여기 HTTP 헤더에 쿠키를 담아서 보내줘야한다고 함.
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });

        console.log(response)


        // Check if the response is ok
        if (!response.ok) {
            throw new Error('Failed to register');
        }

        const data: any = await response.json();
        console.log("데이터",data.status)
// 성공 시 status: true 추가
        return { ...data, status: true }; // 상태 값을 true로 반환

    } catch (e) {
        // Log the error and return an error status
        console.error('Error inserting user:', e);
        return { status: false }; // 실패 시 status 값을 false로 반환
    }

}
*/
