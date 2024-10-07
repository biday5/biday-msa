/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: true,
    experimental: {
        typedRoutes: true,
    },
    images: {
        remotePatterns: [
            {
                protocol: "https",  // 올바른 프로토콜 설정
                hostname: "images.pexels.com",
                port: "",
                pathname: "/**",
            },
            {
                protocol: "https",  // 올바른 프로토콜 설정
                hostname: "images.unsplash.com",
                port: "",
                pathname: "/**",
            },
        ],
    },
    webpack: (config) => {
        config.resolve.fallback = {
            fs: false,
            child_process: false,
            worker_threads: false,
            inspector: false,  // 추가된 inspector
        };
        return config;
    },
};

module.exports = nextConfig;
