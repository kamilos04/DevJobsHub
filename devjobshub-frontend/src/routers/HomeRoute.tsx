import Login from '@/components/register/Login'
import React from 'react'
import { Route, Routes } from 'react-router'

const HomeRoute = () => {
  return (
    <div>
        <Routes>
            <Route path='/login' element={<Login/>}/>
        </Routes>
    </div>
  )
}

export default HomeRoute
