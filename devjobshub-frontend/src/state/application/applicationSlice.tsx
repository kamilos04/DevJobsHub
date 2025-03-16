
import { createSlice } from "@reduxjs/toolkit";
import { applyForOfferById, getApplicationsFromOffer, setApplicationStatus } from "./action";
import { Application } from "@/types/application";


interface PageResponseApplications {
    content: Array<Application> | undefined | null,
    number: number,
    elements: number,
    totalPages: number,
    totalElements: number
}

interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null,
    error: any | null,
    applications: PageResponseApplications | null
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    applications: null
} satisfies InitialState as InitialState

const applicationSlice = createSlice({
    name: 'application',
    initialState,
    reducers: {
        setSuccessNull(state) {
            state.success = null
        },
        setFailNull(state) {
            state.fail = null
        }
    },
    extraReducers: (builder) => {
        builder.addCase(applyForOfferById.pending, (state) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
            .addCase(applyForOfferById.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "applyForOfferById"
            })
            .addCase(applyForOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "applyForOfferById",
                    state.error = action.payload
            })



            .addCase(getApplicationsFromOffer.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(getApplicationsFromOffer.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "getApplicationsFromOffer"
                state.applications = action.payload
            })
            .addCase(getApplicationsFromOffer.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "getApplicationsFromOffer",
                    state.error = action.payload
                    state.applications = null
            })

            
            .addCase(setApplicationStatus.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(setApplicationStatus.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "setApplicationStatus"
            })
            .addCase(setApplicationStatus.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "setApplicationStatus",
                    state.error = action.payload
            })


    }
})


export default applicationSlice.reducer
export const { setSuccessNull, setFailNull } = applicationSlice.actions