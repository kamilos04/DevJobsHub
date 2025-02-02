import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { fetchProfile, register, login } from "./action";

interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null, 
    error: any | null
    profile: User | undefined | null
}

const initialState = { 
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    profile: undefined} satisfies InitialState as InitialState

const profileSlice = createSlice({
    name: 'profile',
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
        builder.addCase(fetchProfile.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(fetchProfile.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "fetchProfile"
            state.profile = action.payload
        })
        .addCase(fetchProfile.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "fetchProfile",
            state.profile = null,
            state.error = action.payload
        })



        .addCase(register.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(register.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "register"
        })
        .addCase(register.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "register",
            state.error = action.payload
        })


        .addCase(login.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(login.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "login"
        })
        .addCase(login.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "login",
            state.error = action.payload
        })
    }
})


export default profileSlice.reducer
export const {setSuccessNull, setFailNull} = profileSlice.actions