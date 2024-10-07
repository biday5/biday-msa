import React, { createContext, useContext, ReactNode } from 'react';
import {handleReissueToken} from "@/utils/reissue/reissueToken";

type AuthContextType = {
    reissueToken: () => Promise<void>;
};

// children 타입을 ReactNode로 설정
interface AuthProviderProps {
    children: ReactNode;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const reissueToken = async () => {
        await handleReissueToken(); // 토큰 재발급 함수 실행
    };

    return (
        <AuthContext.Provider value={{ reissueToken }}>
            {children}
        </AuthContext.Provider>
    );
};

// useAuth Hook
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
