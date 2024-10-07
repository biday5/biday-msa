import {BidModel} from "@/model/BidModel";
import {useSelector} from "react-redux";
import {getToken} from "@/lib/features/user.slice";
import Cookies from "js-cookie";

const link = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/bids`


export async function insertBid(bid): Promise<any | { status: number }> {
    
    try {

        console.log("insertBid 호출")

        const token = Cookies.get("token");

        console.log("토큰 확인: ", token);

        const userinfo = {
            userId: bid.userId,
            userName: bid.userName,
            userRole: bid.userRole,
        }

        const body = {
            auctionId: bid.auctionId,
            currentBid: bid.currentBid,
        }

        console.log("바디 이후");

        const response = await fetch(link, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'UserInfo': JSON.stringify(userinfo),
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(body)
            }
        );

        console.log("여기까지 옴?")

        return await response.json();

    } catch (error) {
        console.error("입찰 등록 중 오류 발생: ", error);
        return {status: 500}
    }
}

export async function getBid(id:number): Promise<any | {state: number}> {
    try {
        const response = await fetch(link + "/" + id, {
            method: 'GET'
        });

        const data = await response.json();

        console.log("+++++>" + JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("입찰 개별 데이터 로드 중 오류 발생", error);
        return { status: 500 };
    }
}

export async function getBidListByUserId() {
    try {

    } catch (error) {
        console.error("유저 입찰 리스트 로드 중 오류 발생", error);
        return {state: 500}
    }
}

export async function getBidList() {
    try {
        const response = await fetch(link, {
            method: 'GET'
        });

        const data = await response.json();

        console.log("+++++>" + JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("입찰 리스트 로드 중 오류 발생", error);
        return { status: 500 };
    }
}


export async function deleteBid(id:number) {
    try {

        const response = await fetch(link + "/" + id, {
            method: 'DELETE'
            // 토큰 필요
        });

        const data: any = await response;

        return data;

    } catch (error) {
        console.error("입찰 개별 삭제 중 오류 발생", error);

        return {status: 500}
    }

}