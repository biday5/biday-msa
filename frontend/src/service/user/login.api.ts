// src/service/user/login.api.ts
import {getCookie} from "@/utils/cookie/cookie.api";
import {extractUserInfoFromToken} from "@/utils/jwt.utils";


export const handleLogin = async (username: string, password: string): Promise<Response | null> => {
    try {
        // JSON 형식으로 데이터 구성
        const body = JSON.stringify({
            username: username,
            password: password,
        });

        const response = await fetch("http://localhost:8000/login", {
            cache: "no-store",
            method: "POST",
            headers: {
                "Content-Type": "application/json",  // JSON 형식으로 전송
            },
            body: body,  // JSON 데이터를 body에 담아서 전송
            credentials: "include",  // 쿠키 포함
        });
        const headers = {
            "Content-Type ": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("accessToken")}`
        }

        if (response.ok) {
            // 서버에서 'Authorization' 헤더에 액세스 토큰을 담아 보냈다면, 이를 추출
            const authorizationHeader = response.headers.get("Authorization");
            if (authorizationHeader) {
                // "Bearer {accessToken}" 형태로 전송되므로 'Bearer' 부분을 제거
                const accessToken = authorizationHeader.split(" ")[1];
            }
            // 리프레시 토큰은 쿠키에 저장되어 있으므로, 별도의 처리 필요 없음
            const refreshToken = document.cookie.split('; ').find(row => row.startsWith('refresh='));
            const tokenValue = refreshToken ? refreshToken.split('=')[1] : '';
            // const refreshToken = getCookie("refresh");  // 쿠키에서 리프레시 토큰 가져오기

            console.log("토큰이 들어 있는지 확인 하는 코드  : " , headers)
            // 응답이 비어있지 않을 때만 JSON 파싱
            const data = response.headers.get('content-length') !== '0' ? await response.json() : {};
            // 필요한 경우, 추가 처리
            return response;
        } else {
            // 응답이 성공적이지 않은 경우 에러 처리
            throw new Error(response.statusText);
        }
    } catch (jsonError) {
        console.error('JSON Error:', jsonError);
        throw jsonError;
    }
};

// 유저 정보를 보내는 함수 // 객체를 만들어서 보내줘야한다. 공통으로 쓸 수 잇ㄴ느걸 만들우아햐나다.
export const requestUserInfo = async () => {
    const token = localStorage.getItem("accessToken"); // 로컬 스토리지에서 토큰을 가져옴

    if (!token) {
        throw new Error("Access Token is missing");
    }

    // JWT 토큰에서 유저 정보 추출
    const {id: userId, name: userName, role: userRole} = extractUserInfoFromToken(token);

    const response = await fetch("http://localhost:8080/login", {
        method: "POST",  // HTTP 메서드를 선택
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
            "UserInfo": JSON.stringify({
                userId: userId,
                userName: userName,
                userRole: userRole
            })  // 유저 정보를 UserInfo 헤더에 담음
        },
        body: JSON.stringify({}),
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to send request: ${response.statusText}`);
    }

    const data = await response.json();
    console.log("asdfjkasdfl;kjaf", data)
    return data;
};
