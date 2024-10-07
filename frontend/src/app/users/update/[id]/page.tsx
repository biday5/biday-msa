"use client";

// Import necessary hooks and functions
import { FormEvent, useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { RootState } from "@/lib/store";

// Define the UsersModel interface
interface UsersModel {
    email: string;
    password: string;
    name: string;
    phoneNum: number;
    role: string;
}

// Define the UsersDto interface (same as server response)
interface UsersDto {
    id: number;
    email: string;
    name: string;
    phoneNum: number;
    role: string;
}

// Define API functions inline
const API_BASE_URL = 'http://211.188.54.218:8080/users';

const getUserById = async (id: number): Promise<UsersModel> => {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`);
        if (!response.ok) {
            throw new Error('Failed to fetch user data');
        }
        return response.json();
    } catch (error) {
        console.error("Error fetching user data:", error);
        throw error;
    }
};

const updateUser = async (id: string | string[], user: UsersModel) => {
    try {
        const urlId = Array.isArray(id) ? id.join(',') : id;

        const response = await fetch(`${API_BASE_URL}/${urlId}/update`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });

        const responseText = await response.text();
        if (!response.ok) {
            throw new Error(`Failed to update user data: ${response.status} ${responseText}`);
        }

        // 응답 본문이 비어 있지 않은지 확인 후 JSON으로 파싱
        return responseText ? JSON.parse(responseText) as UsersDto : {}; // 빈 응답 처리
    } catch (error) {
        console.error("Error updating user data:", error);
        throw error;
    }
};

// Update component
export default function Update() {
    // State for form data and error handling
    const [formData, setFormData] = useState<UsersModel | null>(null);
    const [error, setError] = useState<string | null>(null);

    // Router and path parameters
    const router = useRouter();
    const { id } = useParams();

    // Convert id to number if it's a string
    const userId = typeof id === 'string' ? parseInt(id) : NaN;

    useEffect(() => {
        if (!isNaN(userId)) {
            const fetchUserData = async () => {
                try {
                    const user = await getUserById(userId);
                    setFormData(user);
                } catch (error) {
                    console.error("Failed to fetch user data:", error);
                    setError("Failed to fetch user data");
                }
            };

            fetchUserData();
        } else {
            console.warn("Invalid ID found in path params");
        }
    }, [userId]);

    // Handle input changes
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => prevState ? {
            ...prevState,
            [name]: name === "phoneNum" ? parseInt(value) : value
        } : null);
    };

    // Handle form submission
    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (formData) {
            try {
                const result = await updateUser(id, formData);
                if (result) {
                    router.push('/'); // Redirect to home page or other page upon success
                } else {
                    setError('Failed to update user');
                }
            } catch (error) {
                console.error("Failed to update user:", error);
                setError('Failed to update user');
            }
        }
    };

    // Display loading message or form
    if (!formData) return <p>Loading...</p>;

    return (
        <main className="flex min-h-screen flex-col items-center justify-between p-24">
            <form onSubmit={handleSubmit} className="w-full max-w-sm">
                <div className="mb-4">
                    <label htmlFor="email" className="block text-gray-700">Email</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 w-full"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="password" className="block text-gray-700">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 w-full"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="name" className="block text-gray-700">Name</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 w-full"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="phoneNum" className="block text-gray-700">Phone Number</label>
                    <input
                        type="number"
                        id="phoneNum"
                        name="phoneNum"
                        value={formData.phoneNum}
                        onChange={handleChange}
                        required
                        className="border px-2 py-1 w-full"
                    />
                </div>
                <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded"
                >
                    Update
                </button>
                {error && <p className="text-red-500 mt-2">{error}</p>}
            </form>
        </main>
    );
}
