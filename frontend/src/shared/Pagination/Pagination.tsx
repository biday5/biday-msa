"use client"
//src/shared/Pagination/Pagination.tsx
import {CustomLink} from "@/data/types";

import React, {FC} from "react";
import twFocusClass from "@/utils/twFocusClass";
import Link from "next/link";


export interface PaginationProps {
    className?: string;
    currentPage: number,
    totalPages: number,
    onPageChange: (page: number) => void;
}


const Pagination: FC<PaginationProps> = ({className = "", currentPage, totalPages, onPageChange}) => {

    const renderPageNumbers = () => {
        const pageNumbers = [];

        for (let i = 1; i <= totalPages; i++) {
            pageNumbers.push(
                <button
                key={i}
                onClick={()=> onPageChange(i)}
                className={`inline-flex w-11 h-11 items-center justify-center rounded-full ${
                    currentPage ===i
                    ? "bg-primary-6000 text-white"
                        : "bg-white hover:bg-neutral-100 border border-neutral-200 text-neutral-6000 dark:text-neutral-400 dark:bg-neutral-900 dark:hover:bg-neutral-800 dark:border-neutral-700"
                } ${twFocusClass()} ${className}`}
                >
                    {i}
                </button>
            );
        }
        return pageNumbers;
    }

    return (
        <nav className={`nc-Pagination inline-flex space-x-1 text-base font-medium ${className}`}>
            {renderPageNumbers()}
        </nav>
    );
};

export default Pagination;


  /*  const renderItem = (pag: CustomLink, index: number) => {
        if (index === 0) {
            return (
                <span
                    key={index}
                    className={`inline-flex w-11 h-11 items-center justify-center rounded-full bg-primary-6000 text-white ${twFocusClass()}`}
                >
          {pag.label}
        </span>
            );
        }
        // RETURN UNACTIVE PAGINATION
        return (
            <Link
                key={index}
                className={`inline-flex w-11 h-11 items-center justify-center rounded-full bg-white hover:bg-neutral-100 border border-neutral-200 text-neutral-6000 dark:text-neutral-400 dark:bg-neutral-900 dark:hover:bg-neutral-800 dark:border-neutral-700 ${twFocusClass()}`}
                href={pag.href}
            >
                {pag.label}
            </Link>
        );
    };

    return (
        <nav
            className={`nc-Pagination inline-flex space-x-1 text-base font-medium ${className}`}
        >
         {/!*   {DEMO_PAGINATION.map(renderItem)}*!/}
        </nav>
    );
};

export default Pagination;
*/