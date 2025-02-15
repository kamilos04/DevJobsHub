
import { createSlice } from "@reduxjs/toolkit";
import { applyForOfferById } from "./action";


interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null,
    error: any | null,
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
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
        builder.addCase(applyForOfferById.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
            .addCase(applyForOfferById.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "applyForOfferById"
            })
            .addCase(applyForOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "applyForOfferById",
                    state.error = action.payload
            })




    }
})


export default applicationSlice.reducer
export const { setSuccessNull, setFailNull } = applicationSlice.actions