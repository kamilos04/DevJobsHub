import React from 'react'
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



const OffersRecruiterManager = () => {
  const [sortBy, setSortBy] = React.useState<string>("dateTimeOfCreation")
  const [sortDirection, setSortDirection] = React.useState<string>("asc")


  return (
    <div className='flex flex-col'>
      <Navbar />
      <div className='flex flex-col items-center'>
        <div className='mt-8 p-4 w-[90rem] flex flex-col items-center rounded-2xl'>
          <div className='flex flex-row w-full'>
            <div className=' bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-full'>
              <p className='text-xl'>Offers managed by you</p>
            </div>
            <div className='flex flex-col w-full'>
              <div className='flex flex-row space-x-4 items-center w-full'>
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



              </div>




            </div>
          </div>

        </div>

        {/* <Pagination className='mt-3'>
          <PaginationContent>

            <PaginationItem>
              <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                if (pageNumber - 1 >= 0) {
                  // setPageNumber(pageNumber - 1)
                  const par = new URLSearchParams(searchParams)
                  par.set("pageNumber", `${pageNumber - 1}`)
                  setSearchParams(par)
                }

              }} />
            </PaginationItem>
            {[...Array(storeOffer.searchOffers?.totalPages || 0)].map((_, i) => {
              if (i >= pageNumber - 3 && i <= pageNumber + 3) {
                return (
                  <PaginationItem key={i} >
                    <PaginationLink className='cursor-pointer select-none'
                      onClick={() => {
                        // setPageNumber(i)
                        const par = new URLSearchParams(searchParams)
                        par.set("pageNumber", `${i}`)
                        setSearchParams(par)
                      }}

                      isActive={pageNumber === i ? true : false}
                    >{i + 1}</PaginationLink>
                  </PaginationItem>)
              }
            }
            )}
            <PaginationItem>
              <PaginationNext className='cursor-pointer select-none' onClick={() => {
                if (pageNumber + 1 < storeOffer.searchOffers?.totalPages) {
                  // setPageNumber(pageNumber + 1)
                  const par = new URLSearchParams(searchParams)
                  par.set("pageNumber", `${pageNumber + 1}`)
                  setSearchParams(par)
                }
              }} />
            </PaginationItem>
          </PaginationContent>
        </Pagination> */}
      </div>
    </div>
  )
}

export default OffersRecruiterManager
