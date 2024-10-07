/*
'use client';

import {useState} from "react";

export function Variant() {
    const [variantActive, setVariantActive] = useState(0);

    const renderVariants = () => {
        if (!variants || !variants.length) {
            return null;
        }

        return (
            <div>
                <label htmlFor="">
          <span className="text-sm font-medium">
            Color:
            <span className="ml-1 font-semibold">
              {variants[variantActive].name}
            </span>
          </span>
                </label>
                <div className="flex mt-3">
                    {variants.map((variant, index) => (
                        <div
                            key={index}
                            onClick={() => setVariantActive(index)}
                            className={`relative flex-1 max-w-[75px] h-10 sm:h-11 rounded-full border-2 cursor-pointer ${
                                variantActive === index
                                    ? "border-primary-6000 dark:border-primary-500"
                                    : "border-transparent"
                            }`}
                        >
                            <div
                                className="absolute inset-0.5 rounded-full overflow-hidden z-0 object-cover bg-cover"
                                style={{
                                    backgroundImage: `url(${
                                        // @ts-ignore
                                        typeof variant.thumbnail?.src === "string"
                                            ? // @ts-ignore
                                            variant.thumbnail?.src
                                            : typeof variant.thumbnail === "string"
                                                ? variant.thumbnail
                                                : ""
                                    })`,
                                }}
                            ></div>
                        </div>
                    ))}
                </div>
            </div>
        );
    };

}

export function Size() {
    const [sizeSelected, setSizeSelected] = useState(sizes ? sizes[0] : "");

}*/
