import { API_URL } from "@/config/api"
import { createAsyncThunk } from "@reduxjs/toolkit"
import axios from "axios"

export const uploadFileWithPresignedUrl = createAsyncThunk(
    "files/uploadFileWithPresignedUrl",
    async ({presignedUrl, file}: {presignedUrl: String, file: File}, { rejectWithValue }) => {
        try {
            const { data } = await axios.put(`${presignedUrl}`, file, {
                headers: {
                    "Content-Type": file.type
                }
            })
            return data
        }
        catch (error: any) {
            return rejectWithValue(error.response?.data?.message || "Error")
        }
    }
)


export const getPresignedUrlForCv = createAsyncThunk(
    "files/getPresignedUrlForCv",
    async ({offerId, fileExtension}: {offerId: Number, fileExtension: string}, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt")
            const { data } = await axios.get(`${API_URL}/api/presigned-cv/${offerId}?fileExtension=${fileExtension}`, {
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