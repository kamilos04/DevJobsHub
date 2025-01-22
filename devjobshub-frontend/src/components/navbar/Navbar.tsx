import React from 'react'
import { CgProfile } from "react-icons/cg";
import { MdFavoriteBorder } from "react-icons/md";

const Navbar = () => {
    return (
        <div className='flex flex-row h-[5rem] bg-my-background border-b-2 border-s-stone-900 items-center pl-6 pr-6 justify-between'>
            <div >
                <span className='fontLogo text-xl'>DevJobsHub</span>

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
