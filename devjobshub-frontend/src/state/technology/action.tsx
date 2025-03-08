import { API_URL } from "@/config/api";
import { CreateTechnologyRequest } from "@/types/createTechnologyRequest";
import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";




export const searchTechnologies = createAsyncThunk(
    "technology/searchTechnologies",
    async (text: string, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")

            if (jwt) {
                const { data } = await axios.get(`${API_URL}/api/technology/search?text=${text}`, {
                    headers: {
                        Authorization: `Bearer ${jwt}`
                    }
                })
                return data
            }
            else {
                const { data } = await axios.get(`${API_URL}/api/technology/search?text=${text}`)
                return data
            }


        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)

export const getTechnologiesByIds = createAsyncThunk(
    "technology/getTechnologiesByIds",
    async (ids: string, { rejectWithValue }) => {
        try {
            const { data } = await axios.get(`${API_URL}/api/technology/by-ids?ids=${ids}`)
            return data
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)



export const createTechnology = createAsyncThunk(
    "technology/createTechnology",
    async (reqData: CreateTechnologyRequest, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/technology`, reqData, {
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