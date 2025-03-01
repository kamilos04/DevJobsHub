import { Technology } from "@/types/technology";
import { User } from "@/types/user";
import { createSlice } from "@reduxjs/toolkit";
import { createOffer as createOffer, deleteOfferById, getOfferById, getOffersFromRecruiter, likeOfferById, removeLikeOfferById, searchOffers, updateOffer } from "./action";
import { Offer } from "@/types/offer";

interface PageResponseOffers {
    content: Array<Offer> | undefined | null,
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
    searchOffers: PageResponseOffers | undefined | null,
    offer: Offer | null,
    offersFromRecruiter: PageResponseOffers | null
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    searchOffers: null,
    offer: null,
    offersFromRecruiter: null
} satisfies InitialState as InitialState

const offerSlice = createSlice({
    name: 'offer',
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


            .addCase(updateOffer.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
                .addCase(updateOffer.fulfilled, (state, action) => {
                    state.isLoading = false,
                        state.success = "updateOffer"
                })
                .addCase(updateOffer.rejected, (state, action) => {
                    state.isLoading = false,
                        state.fail = "updateOffer",
                        state.error = action.payload
                })




            .addCase(searchOffers.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                // state.searchOffers = null
            })
            .addCase(searchOffers.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "searchOffers"
                state.searchOffers = action.payload
            })
            .addCase(searchOffers.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "searchOffers",
                    state.error = action.payload
                state.searchOffers = null
            })


            .addCase(getOfferById.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                state.offer = null
            })
            .addCase(getOfferById.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "getOfferById"
                state.offer = action.payload
            })
            .addCase(getOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "getOfferById",
                    state.error = action.payload
                state.offer = null
            })


            .addCase(likeOfferById.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(likeOfferById.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "likeOfferById"
            })
            .addCase(likeOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "likeOfferById",
                    state.error = action.payload
            })


            .addCase(removeLikeOfferById.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(removeLikeOfferById.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "removeLikeOfferById"
            })
            .addCase(removeLikeOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "removeLikeOfferById",
                    state.error = action.payload
            })


            .addCase(getOffersFromRecruiter.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                state.offersFromRecruiter = null
            })
            .addCase(getOffersFromRecruiter.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "getOffersFromRecruiter",
                    state.offersFromRecruiter = action.payload

            })
            .addCase(getOffersFromRecruiter.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "getOffersFromRecruiter",
                    state.error = action.payload
            })


            .addCase(deleteOfferById.pending, (state, action) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(deleteOfferById.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "deleteOfferById"
            })
            .addCase(deleteOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "deleteOfferById",
                    state.error = action.payload
            })


    }
})


export default offerSlice.reducer
export const { setSuccessNull, setFailNull } = offerSlice.actions