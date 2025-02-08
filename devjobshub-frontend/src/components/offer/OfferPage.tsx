import React, { useEffect } from 'react'
import { useParams } from 'react-router';
import Navbar from '../navbar/Navbar';
import { useDispatch, useSelector } from 'react-redux';
import { getOfferById } from '@/state/offer/action';
import { MdOutlineWorkOutline } from "react-icons/md";
import { TiDocumentText } from "react-icons/ti";
import { RiStairsLine } from "react-icons/ri";
import { jobLevelsAndLabels } from '@/constants';

const OfferPage = () => {
    const { id } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => store.offer)

    useEffect(() => {
        dispatch(getOfferById(Number(id)))
    }, [])

    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>
                <div className='mt-8 p-4 flex flex-row rounded-2xl gap-x-8'>
                    <div className='bg-my-card flex flex-col p-4 pl-5 rounded-xl border-[1px] w-[50rem]'>
                        <h1 className='text-xl font-bold'>{storeOffer.offer?.name}</h1>
                        <div className='flex flex-row mt-1 mb-2 items-center gap-x-1 text-gray-400'>
                            <MdOutlineWorkOutline className='text-xl' />
                            <span>{storeOffer.offer?.firmName}</span>
                        </div>
                        <div className='flex flex-row'>
                            <div className='flex flex-col'>
                                {/* <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-400'>
                                    <div className='p-4 bg-slate-800 rounded-2xl'>
                                        <TiDocumentText className='text-xl' />
                                    </div>
                                    <span>{storeOffer.offer?.firmName}</span>
                                </div> */}
                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-400'>
                                    <div className='p-4 bg-slate-800 rounded-2xl'>
                                    <RiStairsLine className='text-xl' />
                                    </div>
                                    <span>{jobLevelsAndLabels.map((element: any) => {
                                        if(element.value === storeOffer.offer?.jobLevel){
                                            return element.label
                                        }
                                    })}</span>
                                </div>

                            </div>
                            <div className='flex flex-col'>

                            </div>
                        </div>


                    </div>
                    <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-[25rem] h-min'>

                    </div>

                </div>

            </div>
            {/* </form> */}
        </div>
    )
}

export default OfferPage
