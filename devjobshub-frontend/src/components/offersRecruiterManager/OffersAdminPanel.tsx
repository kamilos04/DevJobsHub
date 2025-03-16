import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"


import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
} from "@/components/ui/select"
import { Offer } from '@/types/offer'
import { Checkbox } from "@/components/ui/checkbox"
import { useLocation} from 'react-router'
import { useDispatch, useSelector } from 'react-redux'
import { useProfile } from '../profile/useProfile'
import { getOffersAdmin } from '@/state/offer/action'
import OfferCardManager from './OfferCardManager'
import { setFailNull, setSuccessNull } from '@/state/offer/offerSlice'
import { useToast } from '@/hooks/use-toast'


const OffersAdminPanel = () => {
    const [sortBy, setSortBy] = React.useState<string>("dateTimeOfCreation")
    const [sortDirection, setSortDirection] = React.useState<string>("asc")
    const [showValidOffers, setShowValidOffers] = React.useState<boolean>(true)
    const [page, setPage] = React.useState<number>(0)
    const location = useLocation()
    const dispatch = useDispatch<any>()
    const { getProfile, profileStore } = useProfile(true, true, true)
    const offerStore = useSelector((store: any) => (store.offer))
    const { toast } = useToast()
  
  
    const dispatchRequest = () => {
      let params = `sortBy=${sortBy}&sortDirection=${sortDirection}&numberOfElements=10&pageNumber=${page}`
      if (showValidOffers === true) {
        params += "&isActive=true"
      }
      dispatch(getOffersAdmin({ params: params }))
    }
  
    useEffect(() => {
      if (profileStore.profile) {
        dispatchRequest()
      }
  
    }, [sortBy, sortDirection, showValidOffers, page, profileStore.profile])
  
  
    useEffect(() => {
      getProfile()
    }, [location.pathname])
  
  
    useEffect(() => {
      if (offerStore.success === "deleteOfferById") {
        dispatch(setSuccessNull())
        dispatchRequest()
        toast({
          variant: "default",
          className: "bg-green-800",
          title: "The offer has been deleted.",
        });
      }
    }, [offerStore.success])
  
  
    useEffect(() => {
      if (offerStore.fail === "deleteOfferById") {
        dispatch(setFailNull())
        toast({
          variant: "destructive",
          title: "Something went wrong.",
        });
      }
    }, [offerStore.fail])
  
    return (
    (
        <div className='flex flex-col'>
          <Navbar />
          <div className='flex flex-col items-center mb-8'>
            <div className='mt-8 p-4 xl:w-[70rem] 2xl:w-[80rem] 3xl:w-[90rem] flex flex-col items-center rounded-2xl'>
              <div className='flex flex-row w-full gap-x-4'>
                <div className='flex flex-col w-full'>
                  <div className='flex flex-row gap-x-6 items-center w-full'>
                    <Select value={sortBy} onValueChange={setSortBy}>
                      <SelectTrigger className="w-auto">
                        <span>Sort by: {sortBy === "dateTimeOfCreation" && "Creation date"}{sortBy === "expirationDate" && "Expiration date"}{sortBy === "name" && "Offer title"}</span>
                      </SelectTrigger>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="dateTimeOfCreation">Creation date</SelectItem>
                          <SelectItem value="expirationDate">Expiration date</SelectItem>
                          <SelectItem value="name">Offer title</SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
    
    
    
    
                    <Select value={sortDirection} onValueChange={setSortDirection}>
                      <SelectTrigger className="w-auto">
                        Sorting direction: {sortDirection === "asc" ? "Ascending" : "Descending"}
                      </SelectTrigger>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="asc">Ascending</SelectItem>
                          <SelectItem value="dsc">Descending</SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
    
                    <div className="flex items-center space-x-2">
                      <Checkbox id="validOffers" onCheckedChange={(value: boolean) => setShowValidOffers(value)} checked={showValidOffers} />
                      <label
                        htmlFor="validOffers"
                        className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                      >
                        Show only valid offers
                      </label>
                    </div>
    
                  </div>
                  <div className='mt-4 flex flex-col gap-y-6'>
                    {offerStore.offersFromRecruiter?.content.map((element: Offer) => <OfferCardManager key={element.id} offer={element} dispatchRequest={dispatchRequest}/>)}
                    {offerStore.offersFromRecruiter?.totalElements === 0 && <span className='text-gray-300 text-2xl'>{"You don't manage any offers yet"}</span>}
                  </div>
    
    
    
                </div>
              </div>
    
            </div>
    
            {(offerStore.offersFromRecruiter?.totalElements > 0) && <Pagination className='mt-3'>
              <PaginationContent>
                <PaginationItem>
                  <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                    if (page - 1 >= 0) {
                      setPage((value) => value - 1)
                    }
    
                  }} />
                </PaginationItem>
                {[...Array(offerStore.offersFromRecruiter?.totalPages || 0)].map((_, i) => {
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
                    if (page + 1 < offerStore.offersFromRecruiter?.totalPages) {
                      setPage((value) => value + 1)
                    }
                  }} />
                </PaginationItem>
              </PaginationContent>
            </Pagination>}
          </div>
        </div>
      )
  )
}

export default OffersAdminPanel
