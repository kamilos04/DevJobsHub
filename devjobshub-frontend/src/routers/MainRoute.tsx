import CreateOffer from '@/components/createOffer/CreateOffer'
import Login from '@/components/loginRegister/Login'
import React from 'react'
import { Route, Routes } from 'react-router'

const MainRoute = () => {
  return (
    <div>
        <Routes>
            <Route path='/login' element={<Login/>}/>
            <Route path='/create-offer' element={<CreateOffer/>}/>
        </Routes>
    </div>
  )
}

export default MainRoute
