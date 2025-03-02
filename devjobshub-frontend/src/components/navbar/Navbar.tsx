import { fetchProfile } from '@/state/profile/action';
import React, { useEffect } from 'react'
import { CgProfile } from "react-icons/cg";
import { MdFavoriteBorder } from "react-icons/md";
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';

const Navbar = () => {
    const navigate = useNavigate()
    const profileStore = useSelector((store: any) => (store.profile))
    const dispatch = useDispatch<any>()

    useEffect(() => {
        dispatch(fetchProfile())
    }, [])

    return (
        <div className='flex flex-row h-[4rem] bg-my-background border-b-2 border-s-stone-900 items-center pl-6 pr-6 justify-between'>
            <div className='cursor-pointer' onClick={() => navigate("/search?pageNumber=0&sortBy=dateTimeOfCreation&sortingDirection=asc")}>
                <span className='fontLogo text-[1.5rem] select-none'><span className='text-blue-500'>Dev</span><span className='text-gray-400'>Jobs</span><span className='text-gray-500'>Hub</span></span>

            </div>
            <div className='flex flex-row gap-x-8 items-center'>
                {(profileStore.profile && profileStore.profile.isFirm===true) && <div className='flex flex-row items-center'>
                    <button className='p-2 hover:text-blue-300' onClick={() => navigate("/recruiter/manager")}>Recruiter panel</button>
                </div>}
                <button onClick={() => navigate("/favourite")}>
                    <MdFavoriteBorder className='text-[2rem]' />
                </button>
                <button>
                    <CgProfile className='text-[2rem]' />
                </button>

            </div>
        </div>
    )
}

export default Navbar
