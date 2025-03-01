import { Technology } from "@/types/technology";
import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { getPresignedUrlForCompanyImage, getPresignedUrlForCv, getPresignedUrlToDownloadCV, uploadFileWithPresignedUrl } from "./action";
import { PresignedUrlResponse } from "@/types/presignedUrlResponse";


interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null,
    error: any | null
    presignedUrlCv: PresignedUrlResponse | null,
    presignedUrlCvToDownload: PresignedUrlResponse | null,
    presignedUrlForCompanyImage: PresignedUrlResponse | null
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    presignedUrlCv: null,
    presignedUrlCvToDownload: null,
    presignedUrlForCompanyImage: null
} satisfies InitialState as InitialState

const filesSlice = createSlice({
    name: 'files',
    initialState,
    reducers: {
        setSuccessNull(state) {
            state.success = null
        },
        setFailNull(state) {
            state.fail = null
        },
        setPresignedUrlCvNull(state){
            state.presignedUrlCv = null
        },
        setPresignedUrlCvToDownloadNull(state){
            state.presignedUrlCvToDownload = null
        },
        setpresignedUrlForCompanyImageNull(state){
            state.presignedUrlForCompanyImage = null
        },
        resetFilesStore(state){
            state = initialState
        }

    },
    extraReducers: (builder) => {
        builder.addCase(uploadFileWithPresignedUrl.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
            .addCase(uploadFileWithPresignedUrl.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "uploadFileWithPresignedUrl"
            })
            .addCase(uploadFileWithPresignedUrl.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "uploadFileWithPresignedUrl",
                    state.error = action.payload
            })


            .addCase(getPresignedUrlForCv.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                state.presignedUrlCv = null
            })
                .addCase(getPresignedUrlForCv.fulfilled, (state, action) => {
                    state.isLoading = false,
                        state.success = "getPresignedUrlForCv"
                        state.presignedUrlCv = action.payload
                })
                .addCase(getPresignedUrlForCv.rejected, (state, action) => {
                    state.isLoading = false,
                        state.fail = "getPresignedUrlForCv",
                        state.error = action.payload
                })



                .addCase(getPresignedUrlToDownloadCV.pending, (state, action) => {
                    state.isLoading = true
                    state.fail = null
                    state.success = null
                    state.error = null
                    state.presignedUrlCvToDownload = null
                })
                    .addCase(getPresignedUrlToDownloadCV.fulfilled, (state, action) => {
                        state.isLoading = false,
                            state.success = "getPresignedUrlToDownloadCV"
                            state.presignedUrlCvToDownload = action.payload
                    })
                    .addCase(getPresignedUrlToDownloadCV.rejected, (state, action) => {
                        state.isLoading = false,
                            state.fail = "getPresignedUrlToDownloadCV",
                            state.error = action.payload
                    })



                    .addCase(getPresignedUrlForCompanyImage.pending, (state, action) => {
                        state.isLoading = true
                        state.fail = null
                        state.success = null
                        state.error = null
                        state.presignedUrlForCompanyImage = null
                    })
                        .addCase(getPresignedUrlForCompanyImage.fulfilled, (state, action) => {
                            state.isLoading = false,
                                state.success = "getPresignedUrlForCompanyImage"
                                state.presignedUrlForCompanyImage = action.payload
                        })
                        .addCase(getPresignedUrlForCompanyImage.rejected, (state, action) => {
                            state.isLoading = false,
                                state.fail = "getPresignedUrlForCompanyImage",
                                state.error = action.payload
                        })

    }
})


export default filesSlice.reducer
export const { setSuccessNull, setFailNull, setPresignedUrlCvNull, setPresignedUrlCvToDownloadNull, setpresignedUrlForCompanyImageNull, resetFilesStore } = filesSlice.actions