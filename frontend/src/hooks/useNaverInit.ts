//src/hooks/useNaverInit.ts
import { useEffect } from 'react';

export const handleNaverInit = () => {
    useEffect(() => {
        const naverLoginScript = document.createElement('script');
        naverLoginScript.src = 'https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js';
        naverLoginScript.async = true;
        document.body.appendChild(naverLoginScript);
        naverLoginScript.onload = () => {
            const naverLogin = new window.naver.LoginWithNaverId({
                clientId: 'RD1oMcYETTxoiJLIsYYS', // Naver 개발자 센터에서 받은 클라이언트 ID
                callbackUrl: 'http://localhost:3000/auth/callback', // 인증 후 리다이렉트될 URL
                isPopup: false, // 팝업을 사용할 경우 true로 설정
                loginButton: { color: 'green', type: 1, height: 10 }
            });
            naverLogin.init();
        };

        return () => {
            document.body.removeChild(naverLoginScript);
        };
    }, [])
}
