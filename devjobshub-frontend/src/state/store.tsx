import { configureStore } from "@reduxjs/toolkit";
import profileReducer from "./profile/profileSlice"
import technologyReducer from "./technology/technologySlice"

export const store = configureStore({
    reducer: {
        profile: profileReducer,
        technology: technologyReducer
    }
})