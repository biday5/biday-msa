//src/model/user.model.ts
export interface UserModel {
    id?: string;
    oauthName?: string;
    name?: string;
    email?: string;
    password?: string;
    phoneNum?: string;
    createdAt?: Date;
    updatedAt?: Date;
    status?: string;
    totalRating?: number;

    newPassword:string;
    //role:List<Role>; 이거 강사님한테 여쭤보기.
}


// initialUser 설정
export const initialUser: UserModel = {
    id: '',
    oauthName: '',
    name: '',
    email: '',
    password: '',
    phoneNum: '',
    createdAt: new Date(),
    updatedAt: new Date(),
    status: '',
    totalRating: 0,
    newPassword:"",
};



/*
//src/model/user.model.ts
export interface UserModel {
    id?: string;
    oauthName?: string;
    name?: string;  // Optional로 변경
    username?: string;
    email: string;
    password: string;
    phoneNum?: string;  // Optional로 변경
    zipcode?: string;  // Optional로 변경
    streetAddress?: string;  // Optional로 변경
    detailAddress?: string;  // Optional로 변경
    addressType?: string;  // Optional로 변경
    createdAt?: Date;
    updatedAt?: Date;
    status?: string;
    totalRating?: number;
}
// 데이터 타입 쓰잖아. 이걸 불러올 때 API

//DTo를 위에 만들었으니 엔티티 같은 역할이 필요하다.
export const initialUser: UserModel={
    id:0,
    oauthName:'',
    name:'',
    username: '',
    email:'',
    password:'',
    phoneNum:'',
    zipcode:'',
    streetAddress:'',
    detailAddress:'',
    addressType:'',
    createdAt:new Date,
    updatedAt:new Date,
    status:'',
    totalRating:0
}

/!*생성자를 만들어야 한다. 이 녀석이 있어야 객체가 되고 저장소가 된다.
*
* 자바에서 초기화를 하는 이유는 뭐야? *!/*/
