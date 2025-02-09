import React, { useEffect } from 'react'
import { useParams } from 'react-router';
import Navbar from '../navbar/Navbar';
import { useDispatch, useSelector } from 'react-redux';
import { getOfferById, likeOfferById, removeLikeOfferById } from '@/state/offer/action';
import { MdOutlineWorkOutline } from "react-icons/md";
import { TiDocumentText } from "react-icons/ti";
import { RiStairsLine } from "react-icons/ri";
import { jobLevelsAndLabels, operatingModes, operatingModesAndLabels, specializationsAndLabels } from '@/constants';
import { FaRegBuilding } from "react-icons/fa";
import { BsPersonWorkspace } from "react-icons/bs";
import { IoLocationOutline } from "react-icons/io5";
import { FaMoneyBill1Wave } from "react-icons/fa6";
import { Separator } from '../ui/separator';
import { Technology } from '@/types/technology';
import { Badge } from '../ui/badge';
import { MdOutlineDateRange } from "react-icons/md";
import { calcDaysToExpirationDateFromString, calcSecondsToExpirationDateFromString } from '@/utils/dateUtils';
import { GiPlainCircle } from "react-icons/gi";
import { MdFavoriteBorder } from "react-icons/md";

const OfferPage = () => {
    const { id } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => store.offer)
    const [isLiked, setIsLiked] = React.useState<boolean>(false)

    const contractsStringFromOffer = () => {
        let arrayStrings = []
        if (storeOffer.offer?.isSalaryMonthlyUoP !== null) arrayStrings.push("Employment contract")
        if (storeOffer.offer?.isSalaryMonthlyB2B !== null) arrayStrings.push("B2B")
        if (storeOffer.offer?.isSalaryMonthlyUZ !== null) arrayStrings.push("Order contract")
        let text = arrayStrings.join(", ")
        return text
    }

    const getExpirationDate = () => {
        const days = calcDaysToExpirationDateFromString(storeOffer.offer?.expirationDate)
        const seconds = calcSecondsToExpirationDateFromString(storeOffer.offer?.expirationDate)
        if (days < 0) {
            return ("Offer expired")
        }
        else if (days === 0) {
            if (seconds > 0) {
                return ("Offer expires today")
            }
            return ("Offer expired")
        }
        else {
            return (`Offer valid for ${days} days`)
        }
    }

    const isTechnologySectionNeeded = () => {
        return ((storeOffer.offer?.requiredTechnologies.length !== 0) || (storeOffer.offer?.niceToHaveTechnologies.length !== 0))
    }

    const handleLikeClick = () => {
        if (isLiked === false) {
            dispatch(likeOfferById(storeOffer.offer?.id))
            setIsLiked(true)
        }
        else if (isLiked === true) {
            dispatch(removeLikeOfferById(storeOffer.offer?.id))
            setIsLiked(false)
        }
    }

    useEffect(() => {
        dispatch(getOfferById(Number(id)))
    }, [])

    useEffect(() => {
        if (storeOffer.offer && storeOffer.offer?.isLiked !== null) {
            setIsLiked(storeOffer.offer.isLiked)
        }
    }, [storeOffer.offer])


    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>
                {storeOffer.offer && <div className='mt-8 p-4 flex flex-row rounded-2xl gap-x-8'>
                    <div className='flex flex-col gap-y-8'>
                        <div className='bg-my-card flex flex-col p-8 rounded-xl border-[1px] w-[50rem]'>
                            <div className='flex flex-row justify-between'>
                                <div>
                                    <h1 className='text-2xl font-bold'>{storeOffer.offer?.name}</h1>
                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-1 text-gray-300'>
                                        <MdOutlineWorkOutline className='text-2xl' />
                                        <span className='text-lg'>{storeOffer.offer?.firmName}</span>
                                    </div>
                                </div>

                                <MdFavoriteBorder className={`text-3xl cursor-pointer ${isLiked && "text-pink-700"}`} onClick={() => handleLikeClick()} />
                            </div>

                            <Separator className='mt-2 mb-3' />
                            <div className='flex flex-row gap-x-4'>
                                <div className='flex flex-col w-[50%]'>
                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <RiStairsLine className='text-xl text-white' />
                                        </div>
                                        <span>{jobLevelsAndLabels.map((element: any) => {
                                            if (element.value === storeOffer.offer?.jobLevel) {
                                                return element.label
                                            }
                                        })}</span>
                                    </div>
                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <BsPersonWorkspace className='text-xl text-white' />
                                        </div>
                                        <span>{specializationsAndLabels.map((element: any) => {
                                            if (element.value === storeOffer.offer?.specialization) {
                                                return element.label
                                            }
                                        })}</span>
                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <FaRegBuilding className='text-xl text-white' />
                                        </div>
                                        <span>{operatingModesAndLabels.map((element: any) => {
                                            if (element.value === storeOffer.offer?.operatingMode) {
                                                return element.label
                                            }
                                        })}</span>
                                    </div>


                                </div>

                                <div className='flex flex-col w-[50%]'>
                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <TiDocumentText className='text-xl text-white' />
                                        </div>
                                        <span>{contractsStringFromOffer()}</span>
                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <IoLocationOutline className='text-xl text-white' />
                                        </div>
                                        <div className='flex flex-col'>
                                            <span>{storeOffer.offer?.localization}</span>
                                            <span className='text-sm'>{storeOffer.offer?.address}</span>
                                        </div>

                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl'>
                                            <MdOutlineDateRange className='text-xl text-white' />
                                        </div>
                                        <div className='flex flex-col'>
                                            <span>{storeOffer.offer && getExpirationDate()
                                            }</span>
                                            <span className='text-sm'>Expiration date: {storeOffer.offer?.expirationDate.slice(0, -3)}</span>
                                        </div>

                                    </div>
                                </div>
                            </div>

                            <div className='flex flex-row gap-y-3 gap-x-3 flex-wrap mt-3'>
                                {storeOffer.offer?.minSalaryUoP && <div className='p-4 bg-slate-800 rounded-xl flex flex-col gap-y-1 items-start h-min'>
                                    <span className='text-base '>
                                        <span className='font-bold'>{storeOffer.offer?.minSalaryUoP} - {storeOffer.offer?.maxSalaryUoP} zł</span>
                                    </span>
                                    <span className='text-sm'>
                                        Employment contract, gross / {storeOffer.offer?.isSalaryMonthlyUoP === true ? <span>month</span> : <span>hour</span>}
                                    </span>
                                </div>}

                                {storeOffer.offer?.minSalaryB2B && <div className='p-4 bg-slate-800 rounded-xl flex flex-col gap-y-1 items-start h-min'>
                                    <span className='text-base '>
                                        <span className='font-bold'>{storeOffer.offer?.minSalaryB2B} - {storeOffer.offer?.maxSalaryB2B} zł</span>
                                    </span>
                                    <span className='text-sm'>
                                        B2B, net / {storeOffer.offer?.isSalaryMonthlyB2B === true ? <span>month</span> : <span>hour</span>}
                                    </span>
                                </div>}

                                {storeOffer.offer?.minSalaryUZ && <div className='p-4 bg-slate-800 rounded-xl flex flex-col gap-y-1 items-start h-min'>
                                    <span className='text-base '>
                                        <span className='font-bold'>{storeOffer.offer?.minSalaryUZ} - {storeOffer.offer?.maxSalaryUZ} zł</span>
                                    </span>
                                    <span className='text-sm'>
                                        Order contract, gross / {storeOffer.offer?.isSalaryMonthlyUZ === true ? <span>month</span> : <span>hour</span>}
                                    </span>
                                </div>}
                            </div>



                        </div>
                        {(isTechnologySectionNeeded() || storeOffer.offer?.aboutProject || storeOffer.offer?.responsibilitiesText) && <div className='bg-my-card flex flex-col p-8 rounded-xl border-[1px] w-[50rem] gap-y-10'>
                            {isTechnologySectionNeeded() && <div className=''>
                                <p className='text-xl font-bold'>Technologies</p>
                                <div className='flex flex-row'>
                                    {storeOffer.offer?.requiredTechnologies.length !== 0 && <div className='mt-2 flex-1'>
                                        <p className='text-base'>Required</p>
                                        <div className='flex flex-row flex-wrap mt-2 gap-x-3 gap-y-2 pr-4'>
                                            {storeOffer.offer?.requiredTechnologies.map((element: Technology) => <Badge key={element.id} className='cursor-pointer text-sm bg-slate-400'>{element.name}</Badge>)}
                                        </div>


                                    </div>}
                                    {storeOffer.offer?.niceToHaveTechnologies.length !== 0 && <div className='mt-2 flex-1'>
                                        <p className='text-base'>Nice to have</p>
                                        <div className='flex flex-row flex-wrap mt-2 gap-x-3 gap-y-2 pr-4'>
                                            {storeOffer.offer?.niceToHaveTechnologies.map((element: Technology) => <Badge key={element.id} className='cursor-pointer text-sm bg-slate-400'>{element.name}</Badge>)}
                                        </div>
                                    </div>}
                                </div>
                            </div>}

                            {storeOffer.offer?.aboutProject && <div className='flex flex-col gap-y-2'>
                                <span className='text-xl font-bold'>About the project</span>
                                <p className='text-gray-300 whitespace-pre-line'>{storeOffer.offer?.aboutProject}</p>
                            </div>}

                            {storeOffer.offer?.responsibilitiesText && <div className='flex flex-col gap-y-2'>
                                <p className='text-xl font-bold'>Your responsibilities</p>
                                <p className='text-gray-300 whitespace-pre-line'>{storeOffer.offer?.responsibilitiesText}</p>
                            </div>}


                        </div>}

                        {(storeOffer.offer?.requirements.length !== 0 || storeOffer.offer?.responsibilities.length !== 0 || storeOffer.offer?.niceToHave.length !== 0 || storeOffer.offer?.whatWeOffer.length !== 0) && <div className='bg-my-card flex flex-col p-8 rounded-xl border-[1px] w-[50rem] gap-y-10'>
                            {storeOffer.offer?.requirements.length !== 0 && <div className='flex flex-col gap-y-2'>
                                <p className='text-xl font-bold'>Our requirements</p>
                                {storeOffer.offer?.requirements.map((element: string, index: number) => <div className='flex flex-row items-center gap-x-4' key={index}>
                                    <GiPlainCircle className='text-xs' />
                                    <span className='text-gray-300'>{element}</span>
                                </div>)}
                            </div>}

                            {storeOffer.offer?.responsibilities.length !== 0 && <div className='flex flex-col gap-y-2'>
                                <p className='text-xl font-bold'>Responsibilities</p>
                                {storeOffer.offer?.responsibilities.map((element: string, index: number) => <div className='flex flex-row items-center gap-x-4' key={index}>
                                    <GiPlainCircle className='text-xs' />
                                    <span className='text-gray-300'>{element}</span>
                                </div>)}
                            </div>}

                            {storeOffer.offer?.niceToHave.length !== 0 && <div className='flex flex-col gap-y-2'>
                                <p className='text-xl font-bold'>Nice to have</p>
                                {storeOffer.offer?.niceToHave.map((element: string, index: number) => <div className='flex flex-row items-center gap-x-4' key={index}>
                                    <GiPlainCircle className='text-xs' />
                                    <span className='text-gray-300'>{element}</span>
                                </div>)}
                            </div>}

                            {storeOffer.offer?.whatWeOffer.length !== 0 && <div className='flex flex-col gap-y-2'>
                                <p className='text-xl font-bold'>We offer you:</p>
                                {storeOffer.offer?.whatWeOffer.map((element: string, index: number) => <div className='flex flex-row items-center gap-x-4' key={index}>
                                    <GiPlainCircle className='text-xs' />
                                    <span className='text-gray-300'>{element}</span>
                                </div>)}
                            </div>}
                        </div>}


                    </div>

                    <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-[25rem] h-min'>

                    </div>

                </div>}

            </div>
            {/* </form> */}
        </div>
    )
}

export default OfferPage
