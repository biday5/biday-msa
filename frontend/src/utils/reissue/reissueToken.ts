import Cookies from 'js-cookie';  // js-cookie 라이브러리 사용

export const handleReissueToken = async () => {
    try {
        // 쿠키에서 refreshToken을 가져옴
        // 리프레시 토큰은 쿠키에 저장되어 있으므로, 별도의 처리 필요 없음
        const refreshToken = document.cookie.split('; ').find(row => row.startsWith('refresh='));
        const tokenValue = refreshToken ? refreshToken.split('=')[1] : '';


        console.log('refreshToken:', refreshToken);  // 쿠키에 저장된 리프레시 토큰 확인
        // 서버로 리프레시 토큰 전송하여 액세스 토큰 재발급 요청
        const response = await fetch("http://localhost:8000/reissue", {
            method: "POST",
            credentials: 'include',  // 쿠키 포함
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ tokenValue }),  // JSON 형식으로 리프레시 토큰 전송
        });

        if (response.ok) {
            const data = await response.json();
            console.log("재발급된 데이터:", data);  // 서버에서 반환된 데이터 확인
            //alert('Token reissued successfully!');
            Cookies.set('accessToken', data.accessToken, { expires: 1, sameSite: 'strict' }); // 1일간 유효한 쿠키 설정
        } else {
            // 요청 실패 시 상태 코드에 따른 에러 처리
            alert(`Error reissuing token: ${response.statusText}`);
        }
    } catch (error) {
        if (error instanceof Error) {
            alert(`Fetch error: ${error.message}`);
        } else {
            alert('An unknown error occurred');
        }
    }
};
