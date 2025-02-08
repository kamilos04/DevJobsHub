import { Technology } from "@/types/technology";
import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { createTechnology, getTechnologiesByIds, searchTechnologies } from "./action";

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
    technologies: PageResponseTechnologies | undefined | null,
    technologiesByIds: Array<Technology> | null
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    technologies: undefined,
    technologiesByIds: null
} satisfies InitialState as InitialState

const technologySlice = createSlice({
    name: 'technology',
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


            .addCase(getTechnologiesByIds.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(getTechnologiesByIds.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "getTechnologiesByIds",
                    state.technologiesByIds = action.payload
            })
            .addCase(getTechnologiesByIds.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "getTechnologiesByIds",
                    state.error = action.payload
            })


    }
})


export default technologySlice.reducer
export const { setSuccessNull, setFailNull } = technologySlice.actions