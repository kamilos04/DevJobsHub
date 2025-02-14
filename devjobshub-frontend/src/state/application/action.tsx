import { API_URL } from "@/config/api";
import { ApplyRequest } from "@/types/applyRequest";
import { CreateOfferRequest } from "@/types/createOfferRequest";
import { CreateTechnologyRequest } from "@/types/createTechnologyRequest";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";


export const applyForOfferById = createAsyncThunk(
    "application/applyForOfferById",
    async ( {id, request}: {id: number, request: ApplyRequest}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/apply/${id}`, request, {
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


