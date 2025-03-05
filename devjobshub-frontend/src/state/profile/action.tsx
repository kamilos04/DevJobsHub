import { API_URL } from "@/config/api";
import { ChangePasswordRequest } from "@/types/changePasswordRequest";
import { LoginRequest } from "@/types/loginRequest";
import { RegisterRequest } from "@/types/registerRequest";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";


export const fetchProfile = createAsyncThunk(
    "profile/fetchProfile",
    async (_: void, { rejectWithValue }: any) => {
        try {
            const jwt = localStorage.getItem("jwt")

            if (!jwt) {
                return rejectWithValue("no token");
            }

            const { data } = await axios.get(`${API_URL}/api/my-profile`, {
                headers: {
                    Authorization: `Bearer ${jwt}`
                }
            }
            )
            return data
        }
        catch (error: any) {
            localStorage.removeItem("jwt")
            return rejectWithValue(error.response.data.message)
        }
    }
)


export const register = createAsyncThunk(
    "profile/register",
    async (reqData: RegisterRequest, { rejectWithValue }) => {
        try {
            const { data } = await axios.post(`${API_URL}/auth/register`, reqData)

            localStorage.setItem("jwt", data.token)
            return data
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)


export const login = createAsyncThunk(
    "profile/login",
    async (reqData: LoginRequest, { rejectWithValue }) => {
        try {
            const { data } = await axios.post(`${API_URL}/auth/login`, reqData)

            localStorage.setItem("jwt", data.token)
            return data
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)



export const changePassword = createAsyncThunk(
    "profile/changePassword",
    async (reqData: ChangePasswordRequest, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/auth/change-password`, reqData, {
                headers: {
                    Authorization: `Bearer ${jwt}`
                }
            })

            return data
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)
