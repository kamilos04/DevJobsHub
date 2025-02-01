import { Technology } from "@/types/technology";
import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { createOffer as createOffer } from "./action";



interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null, 
    error: any | null
}

const initialState = { 
    isLoading: false,
    success: null,
    fail: null,
    error: null} satisfies InitialState as InitialState

const offerSlice = createSlice({
    name: 'offer',
    initialState,
    reducers:{
        setSuccessNull(state) {
            state.success=null
        },
        setFailNull(state) {
            state.fail=null
        }
    },
    extraReducers: (builder) => {
        builder.addCase(createOffer.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(createOffer.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "createOffer"
        })
        .addCase(createOffer.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "createOffer",
            state.error = action.payload
        })

    }
})


export default offerSlice.reducer
export const {setSuccessNull, setFailNull} = offerSlice.actions