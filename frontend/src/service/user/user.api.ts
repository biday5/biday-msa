// src/service/user/user.api.ts
import { UserModel } from "@/model/UserModel";

// 전략 패턴을 사용을 해야 한다. 7번 리플라이 서비스 점 딜리트 이런 식으로
// 공통 API URL 설정
let url = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/users`;

// 공통 fetch 처리 함수
async function apiRequest(
    endpoint: string,
    method: string = "GET",
    body?:any
): Promise<any> {
    const headers = {
        "Content-Type": "application/json",
    };

    try {
        const response = await fetch(`${url}${endpoint}`, {
            method,
            headers,
            body: body ? JSON.stringify(body) : undefined,
        });


        if (!response.ok) {
            throw new Error(`API 불러오는데 실패 : ${response.status}`);
        }

        const contentType = response.headers.get("Content-Type");
        console.log("콘텐트 타입 서버에서 받은 헤더에 값이 있는지 확인 하는 코드 : " , contentType)

        // 응답이 JSON일 경우 JSON으로 파싱, 그렇지 않으면 텍스트로 처리
        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        } else {
            return await response.text(); // JSON이 아닐 경우 텍스트로 처리
        }
    } catch (error) {
        console.error(`API 에러 요청 : ${endpoint}`, error);
        throw error;
    }
}


// 회원 한 명의 정보를 가져오는 API
export async function findUserById(id: string): Promise< UserModel | null> {
    try {
        const data = await apiRequest(`/findById/${id}`, "GET");  // 경로에 ID 추가
        return data as UserModel;
    } catch (error) {
        console.error(`ID 불러오기 실패 : ${id}`, error);
        return null;  // 오류 발생 시 null 반환
    }
}


// 회원가입 API
export async function insertUser(user: UserModel): Promise<any> {
    const body = {
        name: user.name,
        email: user.email,
        password: user.password,
        phoneNum: user.phoneNum,
        //zipcode: user.zipcode,
        //streetAddress: user.streetAddress,
        //detailAddress: user.detailAddress,
        //type: user.addressType,
    };

    try {
        const data = await apiRequest("/join", "POST", body);
        console.log("유저 등록 성공:", data);
        return { ...data, status: true }; // 성공 시 true 반환
    } catch (error) {
        console.error('Error inserting user:', error);
        return { status: false }; // 실패 시 false 반환
    }
}

// 유저 삭제 API
export async function deleteUser(id: number): Promise<void | { status: number }> {
    console.log("deleteUser API 호출 - ID:", id);
    try {
        await apiRequest(`/${id}/cancel`, "DELETE");
    } catch (error) {
        return { status: 500 };
    }
}

// 유저 업데이트 API
export const updateUser = async (id: string, user: UserModel): Promise<Response> => {
    const body = {
        name: user.name,
        email: user.email,
        password: user.password,
        phoneNum: user.phoneNum,
        //zipcode: user.zipcode,
        //streetAddress: user.streetAddress,
        //detailAddress: user.detailAddress,
        //type: user.addressType,
    };

    return apiRequest(`/${id}`, "PUT", body);
};

// 비밀번호 변경 API
export async function changepass(user: UserModel): Promise<Response> {

    const body = {
        email: user.email,
        password: user.password, // 기존 비밀번호
        newPassword : user.newPassword // 새로운 비밀번호
    };
    console.log("asdflfdsjal  : email" , user.email)
    console.log("asdflfdsjal  : password" , user.password)
    console.log("asdflfdsjal  : new" , user.newPassword)
    console.log("asldf;dsafjlks : " , body)

    try {
        const response = await apiRequest(`/changepass`, "PATCH", body);

        // 응답이 텍스트 또는 JSON일 수 있으므로 둘 다 처리
        if (typeof response === 'string') {
            console.log("비밀번호 변경 성공 제발 됐으면 좋겠다. ", response);
        } else {
            console.log("비밀번호 변경 성공 (JSON 응답):", response);
        }

        return response; // 성공 시 응답 반환
    } catch (error) {
        console.error("비밀번호 변경 실패:", error);
        throw error; // 에러 발생 시 호출한 쪽으로 에러 던짐
    }
}

// 로그아웃 API
export const logoutUser = async (): Promise<void> => {
    // 로컬 스토리지에서 토큰 제거
    localStorage.removeItem('accessToken');

    // 쿠키에서 refreshToken 추출
    const refreshToken = document.cookie.split('; ').find(row => row.startsWith('refresh='));
    const tokenValue = refreshToken ? refreshToken.split('=')[1] : '';

    document.cookie = 'refresh=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

    try {
        const response = await fetch("http://localhost:8000/logout", {
            method: "POST",
            credentials: 'include',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ refreshToken: tokenValue }),
        });

        if (response.ok) {
            console.log('로그아웃 성공');
        } else {
            console.error(`로그아웃 실패: ${response.statusText}`);
        }
    } catch (error) {
        if (error instanceof Error) {
            console.error(`Fetch 에러: ${error.message}`);
        } else {
            console.error('알 수 없는 에러 발생');
        }
    }
};