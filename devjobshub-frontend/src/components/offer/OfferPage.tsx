import React, { useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router';
import Navbar from '../navbar/Navbar';
import { useDispatch, useSelector } from 'react-redux';
import { getOfferById, likeOfferById, removeLikeOfferById, searchOffers } from '@/state/offer/action';
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
import { GrSend } from "react-icons/gr";
import { Button } from "@/components/ui/button"
import { Offer } from '@/types/offer';
import SmallOfferCard from './SmallOfferCard';
import { contractsStringFromOffer, getExpirationDate } from '@/utils/utils';

const OfferPage = () => {
    const { id } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => store.offer)
    const [isLiked, setIsLiked] = React.useState<boolean>(false)


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


    }, [id])


    useEffect(() => {
        if (storeOffer.success === "getOfferById") {
            let params = ""
            params += `specializations=${storeOffer.offer.specialization}&`
            params += "numberOfElements=6&"
            params += `pageNumber=0&`
            params += `sortBy=expirationDate&`
            params += `sortingDirection=asc&`
            dispatch(searchOffers(params))
        }
    }, [storeOffer.success])

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
                                    <h1 className='text-2xl font-bold text-blue-400'>{storeOffer.offer?.name}</h1>
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
                                        <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
                                            <RiStairsLine className='text-xl text-white' />
                                        </div>
                                        <span>{jobLevelsAndLabels.map((element: any) => {
                                            if (element.value === storeOffer.offer?.jobLevel) {
                                                return element.label
                                            }
                                        })}</span>
                                    </div>
                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
                                            <BsPersonWorkspace className='text-xl text-white' />
                                        </div>
                                        <span>{specializationsAndLabels.map((element: any) => {
                                            if (element.value === storeOffer.offer?.specialization) {
                                                return element.label
                                            }
                                        })}</span>
                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
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
                                        <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                            <TiDocumentText className='text-xl text-white' />
                                        </div>
                                        <span>{contractsStringFromOffer(storeOffer.offer)}</span>
                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                            <IoLocationOutline className='text-xl text-white' />
                                        </div>
                                        <div className='flex flex-col'>
                                            <span>{storeOffer.offer?.localization}</span>
                                            <span className='text-sm'>{storeOffer.offer?.address}</span>
                                        </div>

                                    </div>

                                    <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                        <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                            <MdOutlineDateRange className='text-xl text-white' />
                                        </div>
                                        <div className='flex flex-col'>
                                            <span>{storeOffer.offer && getExpirationDate(storeOffer.offer)
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
                    <div className='flex flex-col gap-y-10 w-[25rem]'>
                        <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] h-min items-center gap-y-3'>
                            {calcSecondsToExpirationDateFromString(storeOffer.offer.expirationDate) > 0 ? 
                            <Button className='flex flex-row w-48 gap-x-2 h-12 rounded-3xl'>
                                <GrSend className='scale-125' /> <span className='text-lg'>Apply</span>
                            </Button> : 
                            <Button className='flex flex-row w-48 gap-x-2 h-12 rounded-3xl' variant={'secondary'} disabled>
                                <GrSend className='scale-125' /> <span className='text-lg'>Offer expired</span>
                            </Button>}
                            
                            
                            <p className='text-sm text-gray-500'>By clicking apply you consent to the processing of your personal data.</p>

                        </div>
                        <div className='flex flex-col gap-y-2'>
                            <p className='text-xl font-bold'>Check other offers:</p>
                            <div className='flex flex-col gap-y-5'>
                                {storeOffer.searchOffers?.content.map((element: Offer) => {
                                    if(element.id !== storeOffer.offer.id){
                                        return(<SmallOfferCard key={element.id} offer={element}/>)
                                    }
                                    return(<></>)
                                    })}
                            </div>
                        </div>


                    </div>





                </div>}

            </div>
            {/* </form> */}
        </div>
    )
}

export default OfferPage
