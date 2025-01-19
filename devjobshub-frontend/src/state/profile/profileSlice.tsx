import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { isAbsolute } from "path";
import { fetchProfile } from "./action";

interface InitialState {
    isLoading: boolean,
    success: boolean,
    fail: boolean, 
    error: any | null
    profile: User | undefined | null
}

const initialState = { 
    isLoading: false,
    success: false,
    fail: false,
    error: null,
    profile: undefined} satisfies InitialState as InitialState

const profileSlice = createSlice({
    name: 'profile',
    initialState,
    reducers:{},
    extraReducers: (builder) => {
        builder.addCase(fetchProfile.pending, (state, action) => {
            state.isLoading = true
            state.fail = false
            state.success = false
            state.error = null
        })
        .addCase(fetchProfile.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = true
            state.profile = action.payload
        })
        .addCase(fetchProfile.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = true,
            state.profile = null,
            state.error = action.error
        })
    }
})


export default profileSlice.reducer