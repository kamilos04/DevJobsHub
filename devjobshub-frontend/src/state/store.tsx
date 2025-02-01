import { configureStore } from "@reduxjs/toolkit";
import profileReducer from "./profile/profileSlice"
import technologyReducer from "./technology/technologySlice"
import offerReducer from "./offer/offerSlice"

export const store = configureStore({
    reducer: {
        profile: profileReducer,
        technology: technologyReducer,
        offer: offerReducer
    }
})