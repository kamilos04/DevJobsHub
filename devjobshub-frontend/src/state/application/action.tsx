import { API_URL } from "@/config/api";
import { ApplyRequest } from "@/types/applyRequest";
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


export const getApplicationsFromOffer = createAsyncThunk(
    "application/getApplicationsFromOffer",
    async ( {id, params}: {id: number, params: string}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.get(`${API_URL}/api/application/from-offer/${id}?${params}`, {
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


export const setApplicationStatus = createAsyncThunk(
    "application/setApplicationStatus",
    async ( {id, status}: {id: number, status: string}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/application/set-status?applicationId=${id}&status=${status}`, null, {
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
