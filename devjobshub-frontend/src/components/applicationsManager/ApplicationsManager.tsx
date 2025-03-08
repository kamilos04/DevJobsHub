import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useLocation, useNavigate, useParams } from 'react-router';
import { Button } from '../ui/button';
import { MdOutlineDateRange, MdOutlineWorkOutline } from 'react-icons/md';
import { IoLocationOutline } from 'react-icons/io5';
import { TiDocumentText } from 'react-icons/ti';
import { FaRegBuilding } from 'react-icons/fa';
import { BsPersonWorkspace } from 'react-icons/bs';
import { RiStairsLine } from 'react-icons/ri';
import Navbar from '../navbar/Navbar';
import { jobLevelsAndLabels, operatingModesAndLabels, specializationsAndLabels } from '@/constants';
import { contractsStringFromOffer, getExpirationDate } from '@/utils/utils';
import { useProfile } from '../profile/useProfile';
import { getOfferById } from '@/state/offer/action';
import {
    Tabs,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs"
import { Accordion } from '@radix-ui/react-accordion';
import { getApplicationsFromOffer } from '@/state/application/action';
import { Application } from '@/types/application';
import { ApplicationAccordion } from './ApplicationAccordion';
import { IoMdArrowRoundBack } from "react-icons/io";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
  } from "@/components/ui/pagination"
import { setFailNull, setSuccessNull } from '@/state/application/applicationSlice';
import { MAIN_URL } from '@/config/mainConfig';
import { setFailNull as setFailOfferNull } from '@/state/offer/offerSlice';

export const ApplicationsManager = () => {
    const { offerId } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => store.offer)
    const { getProfile, profileStore } = useProfile(true, true)
    const location = useLocation()
    const [tab, setTab] = React.useState<string>("ALL")
    const [page, setPage] = React.useState<number>(0)
    const applicationStore = useSelector((store: any) => (store.application))
    const navigate = useNavigate()

    useEffect(() => {
        getProfile()
        dispatch(getOfferById(Number(offerId)))
    }, [location.pathname])


    const dispatchRequest = () => {
        if (storeOffer.offer) {
            let params = `numberOfElements=10&pageNumber=${page}`
            if (tab !== "ALL") {
                params += `&status=${tab}`
            }
            dispatch(getApplicationsFromOffer({ id: Number(offerId), params: params }))
        }
    }

    useEffect(() =>{
        if(storeOffer.fail === "getOfferById"){
            dispatch(setFailOfferNull())
            navigate(MAIN_URL)
        }
    }, [storeOffer.fail])


    useEffect(() => {
        dispatchRequest()
    }, [storeOffer, tab, page])

    useEffect(() => {
        if (applicationStore.success === "setApplicationStatus") {
            dispatch(setSuccessNull())
            dispatchRequest()
        }
    }, [applicationStore.success])


    useEffect(() => {
        if(applicationStore.fail === "getApplicationsFromOffer"){
            dispatch(setFailNull())
            navigate(MAIN_URL)
        }
    }, [applicationStore.fail])



    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>
                {(storeOffer.offer && profileStore.profile?.isFirm) && <div className='flex flex-col mt-8 bg-my-card p-8 rounded-xl border-[1px] w-[60rem] items-center'>
                    <div className='flex flex-row items-start w-full mb-2'>
                        <Button className='flex flex-row gap-x-1' onClick={() => navigate("/recruiter/manager")}><IoMdArrowRoundBack />Offers manager</Button>
                    </div>
                    <div className='flex flex-col'>
                        <p className='text-3xl font-bold'>{storeOffer.offer.name}</p>
                        <div className='flex flex-row mt-1 mb-2 items-center gap-x-1 text-gray-300'>
                            <MdOutlineWorkOutline className='text-2xl' />
                            <span className='text-lg'>{storeOffer.offer?.firmName}</span>
                        </div>
                    </div>
                    <div className=''>
                        <div className='flex flex-row gap-x-20 mt-4'>
                            <div className='flex flex-col'>
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

                            <div className='flex flex-col w-[20rem]'>
                                {contractsStringFromOffer(storeOffer.offer) && <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                        <TiDocumentText className='text-xl text-white' />
                                    </div>
                                    <span>{contractsStringFromOffer(storeOffer.offer)}</span>
                                </div>}

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
                    </div>
                    <div className='w-full flex flex-row justify-center mt-16 mb-4'>
                        <Tabs defaultValue="account" className="w-[40rem]" value={tab} onValueChange={(value: string) => {setPage(0)
                            setTab(value)
                        }}>
                            <TabsList className="grid w-full grid-cols-4">
                                <TabsTrigger value="ALL">All</TabsTrigger>
                                <TabsTrigger value="NO_STATUS">Without status</TabsTrigger>
                                <TabsTrigger value="FAVOURITE">Favourites</TabsTrigger>
                                <TabsTrigger value="REJECTED">Rejected</TabsTrigger>
                            </TabsList>
                        </Tabs>
                    </div>
                    <div className='flex flex-col w-full'>
                        <Accordion type="single" collapsible className="w-full">
                            {applicationStore.applications?.content.map((element: Application) => <ApplicationAccordion key={element.id} application={element} offer={storeOffer.offer}/>)}
                            {applicationStore.applications?.totalElements === 0 && <div className='text-gray-300 text-xl mt-6'>{"There are no applications yet."}</div>}
                        </Accordion>
                    </div>
                    {(applicationStore.applications?.totalElements > 0) && <Pagination className='mt-3'>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                                    if (page - 1 >= 0) {
                                        setPage((value) => value - 1)
                                    }

                                }} />
                            </PaginationItem>
                            {[...Array(applicationStore.applications.totalPages || 0)].map((_, i) => {
                                if (i >= page - 3 && i <= page + 3) {
                                    return (
                                        <PaginationItem key={i} >
                                            <PaginationLink className='cursor-pointer select-none'
                                                onClick={() => {
                                                    setPage(i)
                                                }}

                                                isActive={page === i ? true : false}
                                            >{i + 1}</PaginationLink>
                                        </PaginationItem>)
                                }
                            }
                            )}
                            <PaginationItem>
                                <PaginationNext className='cursor-pointer select-none' onClick={() => {
                                    if (page + 1 < applicationStore.applications.totalPages) {
                                        setPage((value) => value + 1)
                                    }
                                }} />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>}


                </div>}

            </div>
        </div>
    )
}
