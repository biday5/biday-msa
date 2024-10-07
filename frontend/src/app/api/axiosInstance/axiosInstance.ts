//src/app/api/axiosInstance/axiosInstance.ts
import axios from 'axios';
import Cookies from 'js-cookie'; // 쿠키를 사용하기 위해 js-cookie 임포트

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8000',  // 서버의 기본 URL 설정
    withCredentials: true  // 쿠키를 포함하여 요청을 보내도록 설정
});

// 요청 인터셉터 설정 (쿠키에서 토큰 가져오기)
axiosInstance.interceptors.request.use(
    (config) => {
        const token = Cookies.get('accessToken'); // 쿠키에서 토큰 가져옴.
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;  // 헤더에 토큰 추가
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 인터셉터 설정 (401 응답 시 토큰 재발급)
axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        if (error.response && error.response.status === 401) {
            try {
                const refreshToken = Cookies.get('refreshToken'); // 쿠키에서 리프레시 토큰을 가져옴
                if (!refreshToken) {
                    throw new Error('리프레시 토큰이 유효하지 않습니다.');
                }

                // 리프레시 토큰으로 새로운 액세스 토큰 발급 요청
                const response = await axiosInstance.post('/refresh', { refreshToken });
                const newAccessToken = response.data.accessToken;

                // 새로운 액세스 토큰을 쿠키에 저장
                Cookies.set('accessToken', newAccessToken, {
                    expires: 7,  // 7일 동안 유효
                    secure: true,  // HTTPS에서만 전송
                    sameSite: 'strict',  // CSRF 방지
                });

                // 기존 요청에 새로운 액세스 토큰으로 헤더를 갱신하여 재요청
                error.config.headers['Authorization'] = `Bearer ${newAccessToken}`;
                return axiosInstance(error.config); // 원래 요청을 다시 보냄
            } catch (refreshError) {
                console.error('토큰 재발급 실패:', refreshError);
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;

// 토큰,쿠키, 쿠키를 리덕스에 스토어에 저장을 하고 가져오는
// 로컬스토리지가 털릴 위험이 있으니 , 리덕스에 상태 관리를 하는데, 유저 슬라이스가 있잖아.
// 거기에 유저의 정보를 적으면 저장을 하고, 필요하면 취하라는거네, 로컬 스토리지에 넣으면 털리니 / 그래서 리덕스를 스는거고,
// 리덕스에는 로컬 스토리지에 털려도 되는 정보, 리덕스는 애초에 상태 관리를 해야 하기 때문에,
// 리덕스는 상태관리잖아. 그래서 백에서 데이터 가져오잖아. 그거를 맨 처음에 다 갖고와설 ㅣㄷ거스에 잠시 저장하고 임시 보호를 한 다으멩
// 유저가 홈페이지에 들어갔을 때 한번에 적어오ㅓ고, 조금씩 가져오니깐, 리덕스에는 털리면 안되는 정보가 들어가야한다.


// 리덕스도 털릴 위험이 있기 때문에,