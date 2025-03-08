import { createSlice } from "@reduxjs/toolkit";
import { addRecruiter, createOffer as createOffer, deleteOfferById, getLikedOffers, getOfferById, getOffersFromRecruiter, likeOfferById, removeLikeOfferById, removeRecruiter, searchOffers, searchOffersSideBar, updateOffer } from "./action";
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
    offersFromRecruiter: PageResponseOffers | null,
    likedOffers: PageResponseOffers | null,
    searchOffersSideBar: PageResponseOffers | null
}

const initialState = {
    isLoading: false,
    success: null,
    fail: null,
    error: null,
    searchOffers: null,
    offer: null,
    offersFromRecruiter: null,
    likedOffers: null,
    searchOffersSideBar: null
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
        builder.addCase(createOffer.pending, (state) => {
            state.isLoading = true
            state.fail = null
            state.success = null
            state.error = null
        })
            .addCase(createOffer.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "createOffer"
            })
            .addCase(createOffer.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "createOffer",
                    state.error = action.payload
            })


            .addCase(updateOffer.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(updateOffer.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "updateOffer"
            })
            .addCase(updateOffer.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "updateOffer",
                    state.error = action.payload
            })




            .addCase(searchOffers.pending, (state) => {
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



            .addCase(searchOffersSideBar.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                state.searchOffersSideBar = null
            })
            .addCase(searchOffersSideBar.fulfilled, (state, action) => {
                state.isLoading = false,
                    state.success = "searchOffersSideBar"
                state.searchOffersSideBar = action.payload
            })
            .addCase(searchOffersSideBar.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "searchOffersSideBar",
                    state.error = action.payload
                state.searchOffersSideBar = null
            })



            .addCase(getOfferById.pending, (state) => {
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


            .addCase(likeOfferById.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(likeOfferById.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "likeOfferById"
            })
            .addCase(likeOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "likeOfferById",
                    state.error = action.payload
            })


            .addCase(removeLikeOfferById.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(removeLikeOfferById.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "removeLikeOfferById"
            })
            .addCase(removeLikeOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "removeLikeOfferById",
                    state.error = action.payload
            })


            .addCase(getOffersFromRecruiter.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
                // state.offersFromRecruiter = null
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
                    state.offersFromRecruiter = null
            })


            .addCase(deleteOfferById.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null
            })
            .addCase(deleteOfferById.fulfilled, (state) => {
                state.isLoading = false,
                    state.success = "deleteOfferById"
            })
            .addCase(deleteOfferById.rejected, (state, action) => {
                state.isLoading = false,
                    state.fail = "deleteOfferById",
                    state.error = action.payload
            })



            .addCase(getLikedOffers.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null

            })
            .addCase(getLikedOffers.fulfilled, (state, action) => {
                state.isLoading = false
                state.success = "getLikedOffers"
                state.likedOffers = action.payload

            })
            .addCase(getLikedOffers.rejected, (state, action) => {
                state.isLoading = false
                state.fail = "getLikedOffers"
                state.error = action.payload
                state.likedOffers = null
            })


            .addCase(addRecruiter.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null

            })
            .addCase(addRecruiter.fulfilled, (state) => {
                state.isLoading = false
                state.success = "addRecruiter"

            })
            .addCase(addRecruiter.rejected, (state, action) => {
                state.isLoading = false
                state.fail = "addRecruiter"
                state.error = action.payload
            })


            .addCase(removeRecruiter.pending, (state) => {
                state.isLoading = true
                state.fail = null
                state.success = null
                state.error = null

            })
            .addCase(removeRecruiter.fulfilled, (state) => {
                state.isLoading = false
                state.success = "removeRecruiter"

            })
            .addCase(removeRecruiter.rejected, (state, action) => {
                state.isLoading = false
                state.fail = "removeRecruiter"
                state.error = action.payload
            })





    }
})


export default offerSlice.reducer
export const { setSuccessNull, setFailNull } = offerSlice.actions