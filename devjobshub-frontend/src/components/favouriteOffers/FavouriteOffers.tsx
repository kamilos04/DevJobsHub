import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination"


import { Input } from "@/components/ui/input"
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Offer } from '@/types/offer'
import { Controller } from 'react-hook-form'
import { Button } from '../ui/button'
import { Checkbox } from "@/components/ui/checkbox"
import { useLocation, useNavigate } from 'react-router'
import { useDispatch, useSelector } from 'react-redux'
import { useProfile } from '../profile/useProfile'
import { getLikedOffers } from '@/state/offer/action'
import OfferCardManager from './OfferCardManager'
import { setFailNull, setSuccessNull } from '@/state/offer/offerSlice'
import { useToast } from '@/hooks/use-toast'
import OfferCard from '../search/OfferCard'

const FavouriteOffers = () => {
    const [page, setPage] = React.useState<number>(0)
    const location = useLocation()
    const dispatch = useDispatch<any>()
    const navigate = useNavigate()
    const offerStore = useSelector((store: any) => (store.offer))
    const { getProfile, profileStore } = useProfile(true, false)



    useEffect(() => {
        dispatch(getLikedOffers({ params: `numberOfElements=10&pageNumber=${page}` }))
    }, [page])


    useEffect(() => {
        if(offerStore.success === "removeLikeOfferById"){
            dispatch(getLikedOffers({ params: `numberOfElements=10&pageNumber=${page}` }))
        }
    }, [offerStore.success])


    useEffect(() => {
        getProfile()
    }, [location.pathname])
    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center mb-8'>
                <div className='mt-8 p-4 w-[70rem] flex flex-col items-center rounded-2xl'>
                    <h1 className='text-2xl'>Favourite offers {offerStore.likedOffers && <span>{`(${offerStore.likedOffers.totalElements})`}</span>}</h1>
                    <div className='flex flex-row w-full gap-x-4'>
                        <div className='flex flex-col w-full'>

                            <div className='flex flex-col gap-y-4 mt-4 w-full'>
                                {offerStore.likedOffers?.content?.map((element: Offer) => <OfferCard offer={element} key={element.id} />)}
                                {offerStore.likedOffers?.totalElements === 0 && <span className='text-gray-300 text-2xl'>{"You don't have liked offers."}</span>}
                            </div>
                            <div className='mt-4 flex flex-col gap-y-6'>
                                
                            </div>



                        </div>
                    </div>

                </div>

                {(offerStore.likedOffers?.totalElements > 0) && <Pagination className='mt-3'>
                    <PaginationContent>
                        <PaginationItem>
                            <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                                if (page - 1 >= 0) {
                                    setPage((value) => value - 1)
                                }

                            }} />
                        </PaginationItem>
                        {[...Array(offerStore.likedOffers?.totalPages || 0)].map((_, i) => {
                            if (i >= page - 3 && i <= page + 3) {
                                return (
                                    <PaginationItem key={i} >
                                        <PaginationLink className='cursor-pointer select-none'
                                            onClick={() => {
                                                setPage(i)
                                            }}

                                            isActive={page === i ? true : false}
                                        >{i + 1}</PaginationLink>
                                    </PaginationItem>)
                            }
                        }
                        )}
                        <PaginationItem>
                            <PaginationNext className='cursor-pointer select-none' onClick={() => {
                                if (page + 1 < offerStore.likedOffers?.totalPages) {
                                    setPage((value) => value + 1)
                                }
                            }} />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>}
            </div>
        </div>
    )
}

export default FavouriteOffers
