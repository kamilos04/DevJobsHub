import ApplyPage from '@/components/apply/ApplyPage'
import CreateOffer from '@/components/createOffer/CreateOffer'
import Login from '@/components/loginRegister/Login'
import OfferPage from '@/components/offer/OfferPage'
import OffersRecruiterManager from '@/components/offersRecruiterManager/offersRecruiterManager'
import SearchPage from '@/components/search/SearchPage'
import React from 'react'
import { Route, Routes } from 'react-router'

const MainRoute = () => {
  return (
    <div>
        <Routes>
            <Route path='/login' element={<Login/>}/>
            <Route path='/create-offer' element={<CreateOffer/>}/>
            <Route path='/search' element={<SearchPage/>}/>
            <Route path="/offer/:id" element={<OfferPage/>}/>
            <Route path="/apply/:id" element={<ApplyPage/>}/>
            <Route path="/recruiter/manager" element={<OffersRecruiterManager/>}/>
        </Routes>
    </div>
  )
}

export default MainRoute
