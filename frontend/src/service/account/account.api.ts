const baseUrl = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/account`


// 판매자 계좌등록
export function saveAccount() {
    const url = `${baseUrl}/save`

    const response = fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
}

// 판매자 계좌조회