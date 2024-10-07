// global.d.ts
export interface NaverLogin {
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

export interface Window {
    naver: {
        LoginWithNaverId: NaverLogin;
    };
}
