import { ApplicationsManager } from '@/components/applicationsManager/ApplicationsManager'
import ApplyPage from '@/components/apply/ApplyPage'
import CreateOffer from '@/components/createOffer/CreateOffer'
import UpdateOffer from '@/components/createOffer/UpdateOffer'
import FavouriteOffers from '@/components/favouriteOffers/FavouriteOffers'
import Login from '@/components/loginRegister/Login'
import OfferPage from '@/components/offer/OfferPage'
import OffersAdminPanel from '@/components/offersRecruiterManager/OffersAdminPanel'
import OffersRecruiterManager from '@/components/offersRecruiterManager/OffersRecruiterManager'
import BlankPage from '@/components/search/BlankPage'
import SearchPage from '@/components/search/SearchPage'
import { Route, Routes } from 'react-router'

const MainRoute = () => {
  return (
    <div>
        <Routes>
            <Route path="/" element={<BlankPage/>}/>
            <Route path='/login' element={<Login/>}/>
            <Route path='/recruiter/create-offer' element={<CreateOffer/>}/>
            <Route path='/search' element={<SearchPage/>}/>
            <Route path='/favourite' element={<FavouriteOffers/>}/>
            <Route path="/offer/:id" element={<OfferPage/>}/>
            <Route path="/apply/:id" element={<ApplyPage/>}/>
            <Route path="/recruiter/manager" element={<OffersRecruiterManager/>}/>
            <Route path="/recruiter/applications/:offerId" element={<ApplicationsManager/>}/>
            <Route path="/recruiter/update-offer/:offerId" element={<UpdateOffer/>}/>
            <Route path="/admin/offers" element={<OffersAdminPanel/>}/>

        </Routes>
    </div>
  )
}

export default MainRoute
