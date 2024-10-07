// src/app/api/auth/[...nextauth]/route.ts
import NextAuth from 'next-auth';
import NaverProvider from 'next-auth/providers/naver';
import {Provider} from "react";

const handler = NextAuth({
    pages:{
        signIn : 'login',
    },
    providers: [
        NaverProvider({
            clientId: process.env.NAVER_CLIENT_ID as string,
            clientSecret: process.env.NAVER_CLIENT_SECRET as string,
        }),
    ],
    /*callbacks: {
        async signIn({account,user,profile}){
            let body={
                provider: '' as Provider,
                pId:'',
                oauthAccessToken:'',
                oauthRefreshToken:'',
            };

            if (account){
                const {access_token, refresh_token, provider, providerAccountId } =
                    account;

                body = {
                    provider: provider as Provider,
                    pId: providerAccountId as string,
                    oauthAccessToken: access_token as string,
                    oauthRefreshToken: access_token as string,
                };
            }

            try {
                await requestSocialSignin(body);
            } catch (e){
                if((e as AppError).message === "NOT_FOUND_PROVIDER") {
                    return `/signup/terms?socialBody = ${JSON.stringify(body)}`;
                }

                // 에러 처리 해야하나
            }
            return true;
        }
    }*/
});

export { handler as GET, handler as POST };