import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { useNavigate } from 'react-router'

const BlankPage = () => {
    const navigate = useNavigate()
    useEffect(() => {
        navigate("/search?pageNumber=0&sortBy=dateTimeOfCreation&sortingDirection=asc")
    })
  return (
    <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center mb-8'>


            </div>
        </div>
  )
}

export default BlankPage
