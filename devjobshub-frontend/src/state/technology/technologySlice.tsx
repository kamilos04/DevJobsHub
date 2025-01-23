import { Technology } from "@/types/technology";
import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { createTechnology, searchTechnologies } from "./action";

interface PageResponseTechnologies {
    content: Array<Technology> | undefined | null,
    number: number,
    elements: number,
    totalPages: number,
    totalElements: number
}

interface InitialState {
    isLoading: boolean,
    success: string | null,
    fail: string | null, 
    error: any | null
    technologies: PageResponseTechnologies | undefined | null
}

const initialState = { 
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    technologies: undefined} satisfies InitialState as InitialState

const technologySlice = createSlice({
    name: 'technology',
    initialState,
    reducers:{},
    extraReducers: (builder) => {
        builder.addCase(searchTechnologies.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(searchTechnologies.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "searchTechnologies"
            state.technologies = action.payload
        })
        .addCase(searchTechnologies.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "searchTechnologies",
            state.technologies = null,
            state.error = action.payload
        })


        .addCase(createTechnology.pending, (state, action) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
        .addCase(createTechnology.fulfilled, (state, action) => {
            state.isLoading = false,
            state.success = "createTechnology"
        })
        .addCase(createTechnology.rejected, (state, action) => {
            state.isLoading = false,
            state.fail = "createTechnology",
            state.error = action.payload
        })

    }
})


export default technologySlice.reducer