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



export const updateOffer = createAsyncThunk(
    "offer/updateOffer",
    async ({ id, reqData }: { id: number, reqData: CreateOfferRequest }, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.put(`${API_URL}/api/offer/${id}`, reqData, {
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

export const searchOffers = createAsyncThunk(
    "offer/searchOffers",
    async (params: string, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")

            if (jwt) {
                const { data } = await axios.get(`${API_URL}/api/offer/search?${params}`, {
                    headers: {
                        Authorization: `Bearer ${jwt}`
                    }
                })
                return data
            }
            else {
                const { data } = await axios.get(`${API_URL}/api/offer/search?${params}`)
                return data
            }
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)

export const searchOffersSideBar = createAsyncThunk(
    "offer/searchOffersSideBar",
    async (params: string, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")

            if (jwt) {
                const { data } = await axios.get(`${API_URL}/api/offer/search?${params}`, {
                    headers: {
                        Authorization: `Bearer ${jwt}`
                    }
                })
                return data
            }
            else {
                const { data } = await axios.get(`${API_URL}/api/offer/search?${params}`)
                return data
            }
        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)

export const getOfferById = createAsyncThunk(
    "offer/getOfferById",
    async (id: number, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")

            if (jwt) {
                const { data } = await axios.get(`${API_URL}/api/offer/${id}`, {
                    headers: {
                        Authorization: `Bearer ${jwt}`
                    }
                })
                return data
            }
            else {
                const { data } = await axios.get(`${API_URL}/api/offer/${id}`)
                return data
            }


        }
        catch (error: any) {
            return rejectWithValue(error.response.data.message)
        }
    }
)

export const likeOfferById = createAsyncThunk(
    "offer/likeOfferById",
    async (id: number, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/offer/like/${id}`, null, {
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

export const removeLikeOfferById = createAsyncThunk(
    "offer/removeLikeOfferById",
    async (id: number, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/offer/remove-like/${id}`, null, {
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


export const getOffersFromRecruiter = createAsyncThunk(
    "offer/getOffersFromRecruiter",
    async ({ id, params }: { id: number, params: string }, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.get(`${API_URL}/api/offer/from-recruiter/${id}?${params}`, {
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


export const deleteOfferById = createAsyncThunk(
    "offer/deleteOfferById",
    async (id: number, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")

            const { data } = await axios.delete(`${API_URL}/api/offer/${id}`, {
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



export const getLikedOffers = createAsyncThunk(
    "offer/getLikedOffers",
    async ({params }: { params: string }, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.get(`${API_URL}/api/offer/liked?${params}`, {
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



export const addRecruiter = createAsyncThunk(
    "offer/addRecruiter",
    async ({offerId, userEmail}: {offerId: number, userEmail: string}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/offer/add-recruiter?offerId=${offerId}&userEmail=${userEmail}`, null, {
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

export const removeRecruiter = createAsyncThunk(
    "offer/removeRecruiter",
    async ({offerId, userEmail}: {offerId: number, userEmail: string}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.post(`${API_URL}/api/offer/remove-recruiter?offerId=${offerId}&userEmail=${userEmail}`, null, {
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


