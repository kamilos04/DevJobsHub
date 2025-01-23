import { API_URL } from "@/config/api";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";




export const searchTechnologies = createAsyncThunk(
    "technology/searchTechnologies",
    async (text: string, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.get(`${API_URL}/api/technology/search?text=${text}`, {
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