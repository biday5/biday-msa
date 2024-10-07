// global.d.ts
interface NaverLogin {
    new (options: {
        clientId: string;
        callbackUrl: string;
        isPopup?: boolean;
        loginButton?: {
            color?: string;
            type?: number;
            height?: number;
        };
    }): {
        init: () => void;
    };
}

interface Window {
    naver: {
        LoginWithNaverId: NaverLogin;
    };
}
