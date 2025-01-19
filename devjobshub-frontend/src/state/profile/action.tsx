import { API_URL } from "@/config/api";
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