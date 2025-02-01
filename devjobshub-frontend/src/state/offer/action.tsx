import { API_URL } from "@/config/api";
import { CreateOfferRequest } from "@/types/createOfferRequest";
import { CreateTechnologyRequest } from "@/types/createTechnologyRequest";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";




export const createOffer = createAsyncThunk(
    "offer/createOffer",
    async (reqData: CreateOfferRequest, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/offer`, reqData, {
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