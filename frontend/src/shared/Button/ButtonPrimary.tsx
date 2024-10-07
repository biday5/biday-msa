"use client";
import Button, { ButtonProps } from "@/shared/Button/Button";
import React from "react";
import {useRouter} from "next/navigation";
import {Route} from "next";

export interface ButtonPrimaryProps extends ButtonProps {
}


const ButtonPrimary: React.FC<ButtonPrimaryProps> = ({
  className = "",
  ...args
}) => {
    const router = useRouter();
    const onClick = () => {
        if(args.href === undefined) return;
        router.push(args.href);
    };

  return (
    <Button
      className={`ttnc-ButtonPrimary disabled:bg-opacity-90 bg-slate-900 dark:bg-slate-100 hover:bg-slate-800 text-slate-50 dark:text-slate-800 shadow-xl ${className}`}
      onClick={onClick}
      {...args}
    />
  );
};

export default ButtonPrimary;
