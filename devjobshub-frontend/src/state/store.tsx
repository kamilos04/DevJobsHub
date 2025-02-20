import { configureStore } from "@reduxjs/toolkit";
import profileReducer from "./profile/profileSlice"
import technologyReducer from "./technology/technologySlice"
import offerReducer from "./offer/offerSlice"
import applicationReducer from "./application/applicationSlice"
import filesReducer from "./files/filesSlice"

export const store = configureStore({
    reducer: {
        profile: profileReducer,
        technology: technologyReducer,
        offer: offerReducer,
        application: applicationReducer,
        files: filesReducer
    }
})