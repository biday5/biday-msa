//src/components/ SocialSigninGroup.tsx
'use client';

import React from "react";


interface SocialSignInGroupProps {
    style?: React.CSSProperties;
    onSocialSignin: (provider: string) => void;
}

const buttonItems = [
    { type: 'NAVER', label: 'Naver' },
];

const Separator = () => <hr style={{ margin: '10px 0' }} />;

const SocialButton = ({ type, onClick }: { type: string, onClick: () => void }) => (
    <button onClick={onClick} style={{ margin: '5px' }}>
        {type} Login
    </button>
);


const SocialSignInGroup = ({ style, onSocialSignin }: SocialSignInGroupProps) => {
    const onClick = (type: string) => {
        onSocialSignin(type);
    };

    return (
        <div style={style}>
            {buttonItems.map((item, index) => (
                <React.Fragment key={index}>
                    {index !== 0 && <Separator />}
                    <SocialButton type={item.type} onClick={() => onClick(item.type)} />
                </React.Fragment>
            ))}
        </div>
    );
};

export default SocialSignInGroup;