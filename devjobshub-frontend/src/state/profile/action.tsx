import { API_URL } from "@/config/api";
import { RegisterRequest } from "@/types/registerRequest";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";


export const fetchProfile = createAsyncThunk(
    "profile/fetchProfile",
    async () => {
        const jwt = localStorage.getItem("jwt")
        const {data} = await axios.get(`${API_URL}/api/myprofile`, {
                headers:{
                    Authorization: `Bearer ${jwt}`
                }
            }
        )
        return data
    }
)


export const register = createAsyncThunk(
    "profile/register",
    async (reqData: RegisterRequest, {rejectWithValue}) => {
        try{
            const {data} = await axios.post(`${API_URL}/auth/register`, reqData)

        localStorage.setItem("jwt", data.token)
        return data
        }
        catch(error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)