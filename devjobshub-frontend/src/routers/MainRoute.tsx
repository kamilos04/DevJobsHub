import CreateOffer from '@/components/createOffer/CreateOffer'
import Login from '@/components/loginRegister/Login'
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
        </Routes>
    </div>
  )
}

export default MainRoute
