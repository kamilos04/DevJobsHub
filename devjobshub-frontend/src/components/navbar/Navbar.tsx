import React from 'react'
import { CgProfile } from "react-icons/cg";
import { MdFavoriteBorder } from "react-icons/md";

const Navbar = () => {
    return (
        <div className='flex flex-row h-[4rem] bg-my-background border-b-2 border-s-stone-900 items-center pl-6 pr-6 justify-between'>
            <div >
                <span className='fontLogo text-[1.5rem]'><span className='text-blue-500'>Dev</span><span className='text-gray-400'>Jobs</span><span className='text-gray-500'>Hub</span></span>

            </div>
            <div className='flex flex-row'>
                <div>
                    <MdFavoriteBorder className='text-[2rem]'/>
                </div>
                <div className='ml-8'>
                    <CgProfile className='text-[2rem]' />
                </div>
                
            </div>
        </div>
    )
}

export default Navbar
