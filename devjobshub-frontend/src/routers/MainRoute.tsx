import { ApplicationsManager } from '@/components/applicationsManager/ApplicationsManager'
import ApplyPage from '@/components/apply/ApplyPage'
import CreateOffer from '@/components/createOffer/CreateOffer'
import UpdateOffer from '@/components/createOffer/UpdateOffer'
import Login from '@/components/loginRegister/Login'
import OfferPage from '@/components/offer/OfferPage'
import OffersRecruiterManager from '@/components/offersRecruiterManager/OffersRecruiterManager'
import SearchPage from '@/components/search/SearchPage'
import React from 'react'
import { Route, Routes } from 'react-router'

const MainRoute = () => {
  return (
    <div>
        <Routes>
            <Route path='/login' element={<Login/>}/>
            <Route path='/recruiter/create-offer' element={<CreateOffer/>}/>
            <Route path='/search' element={<SearchPage/>}/>
            <Route path="/offer/:id" element={<OfferPage/>}/>
            <Route path="/apply/:id" element={<ApplyPage/>}/>
            <Route path="/recruiter/manager" element={<OffersRecruiterManager/>}/>
            <Route path="/recruiter/applications/:offerId" element={<ApplicationsManager/>}/>
            <Route path="/recruiter/update-offer/:offerId" element={<UpdateOffer/>}/>
        </Routes>
    </div>
  )
}

export default MainRoute
