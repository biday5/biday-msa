//src/utils/auth.ts

export function checkIfLoggedIn() {
    const token = localStorage.getItem("token");
    return !!token;
}

// xhzmsdms ekadm 토큰은 담으면 안되고 이메일 토큰 삭제는 js 토큰에 토// 매번 있는 놈이 로컬스토리지이다. 여기에난 이메일 // 어쩻ㄷㄴ 하나만 넣으면 아쉬우니 객체로 넣기 멤버객체 자체로 넘기면,
// 주소도 넣어도 된다. 회사에서 넣지 말라고 하면 안넣으면 되는데, 여기는 넣으면 간단해지낟.