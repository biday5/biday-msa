// src/types/next-auth.d.ts
import { DefaultSession } from "next-auth";

// 타입 확장을 해야 한다고 함. index.ts에서 에러가 나와서 공식문서 확인. 시발 근데도 안됨
declare module "next-auth" {
    interface Session {
        user: {
            address: string;
        } & DefaultSession["user"];
    }
}