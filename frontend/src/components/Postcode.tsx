// src/components/Postcode.tsx
import React from 'react';
import DaumPostcode from 'react-daum-postcode';


interface PostcodeProps {
    onComplete: (data: any) => void;
    onClose: () => void;  // 주소 검색 완료 후 창을 닫기 위한 함수
}

const Postcode = ({ onComplete, onClose }: PostcodeProps) => {
    const handleComplete = (data: any) => {
        onComplete(data);  // 선택한 주소 데이터 전달
        onClose();  // 창을 닫음
    };

    return (
        <div>
            <DaumPostcode
                onComplete={handleComplete}
                style={{ width: '100%', height: '400px' }}
            />
        </div>
    );
};

export default Postcode;