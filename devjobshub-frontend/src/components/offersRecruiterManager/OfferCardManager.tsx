import { Offer } from '@/types/offer'
import React, { useEffect } from 'react'
import { MdOutlineWorkOutline } from "react-icons/md";
import { IoLocationOutline } from "react-icons/io5";
import { FaRegBuilding } from "react-icons/fa";
import { Separator } from '../ui/separator';
import { RiStairsLine } from "react-icons/ri";
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import { deleteOfferById } from '@/state/offer/action';
import { Button } from '../ui/button';
import { MdOutlineDateRange } from "react-icons/md";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import ManageRecruitersDialog from './ManageRecruitersDialog';

const OfferCardManager = ({ offer, dispatchRequest }: { offer: Offer, dispatchRequest: any }) => {
  const navigate = useNavigate()
  const dispatch = useDispatch<any>()
  const [, setIsLiked] = React.useState<boolean>(false)
  const levels = [
    { value: "TRAINEE", label: "Trainee / Intern" },
    { value: "JUNIOR", label: "Junior" },
    { value: "MID", label: "Mid" },
    { value: "SENIOR", label: "Senior" },
    { value: "MANAGER", label: "Manager" },
    { value: "CLEVEL", label: "C-level" }]

  const jobLevelToLabel = (text: string) => {
    const found = levels.find((element) => element.value === text);
    return found ? found.label : "";
  };



  useEffect(() => {
    if (offer && offer.isLiked !== null) {
      setIsLiked(offer.isLiked)
    }
  }, [offer])



  return (
    <div className='flex flex-col bg-my-card rounded-md border-[1px] pl-3 pr-3 pt-3 pb-3 w-full'>
      <div className='flex flex-row justify-between'>
        <div className='flex flex-row gap-x-3'>
          {offer.imageUrl && <img src={`https://devjobshub.s3.eu-central-1.amazonaws.com/${offer.imageUrl}`} alt="company logo" className='h-20 aspect-square rounded-xl' />}
          <div className='flex flex-col'>
            <h2 className='font-bold text-blue-300'>{offer.name}</h2>
            <div className='flex flex-row mt-1 mb-2 items-center gap-x-1 text-gray-400'>
              <MdOutlineWorkOutline className='text-xl' />
              <span>{offer.firmName}</span>
            </div>

            <div className='flex flex-row gap-x-8'>


              <div className='flex flex-row items-center gap-x-1 text-gray-400'>
                <IoLocationOutline className='text-xl' />
                <span>{offer.localization}</span>
              </div>

              <div className='flex flex-row items-center gap-x-1 text-gray-400'>
                <FaRegBuilding className='text-xl' />
                <span>{offer.operatingMode.charAt(0).toUpperCase() + offer.operatingMode.slice(1).toLocaleLowerCase()}</span>
              </div>

              <div className='flex flex-row items-center gap-x-1 text-gray-400'>
                <RiStairsLine className='text-xl' />
                <span>{jobLevelToLabel(offer.jobLevel)}</span>
              </div>


            </div>
            <div className='flex flex-row items-center gap-x-1 text-gray-400 mt-2'>
              <MdOutlineDateRange className='text-xl' />
              <span>Expiration date: {offer.expirationDate.slice(0, -3)}</span>
            </div>

          </div>
        </div>

        <div className='flex flex-row gap-x-2'>
          <div className='flex flex-col gap-y-2'>
            <ManageRecruitersDialog offer={offer} dispatchRequest={dispatchRequest}/>
          </div>
          <div className='flex flex-col gap-y-2'>
            <Button variant={'outline'} onClick={() => navigate(`/recruiter/applications/${offer.id}`)}>See applications</Button>
            <Button variant={'outline'} onClick={() => navigate(`/recruiter/update-offer/${offer.id}`)}>Update</Button>
          </div>
          <div className='flex flex-col gap-y-2'>
            <Button variant={'outline'} onClick={() => window.open(`/offer/${offer.id}`, "_blank")}>Open the offer</Button>

            <AlertDialog>
              <AlertDialogTrigger asChild>
                <Button variant={'destructive'}>Delete</Button>
              </AlertDialogTrigger>
              <AlertDialogContent>
                <AlertDialogHeader>
                  <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                  <AlertDialogDescription>
                    This action cannot be undone. This will permanently remove the offer and its associated applications.
                  </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                  <AlertDialogCancel>Cancel</AlertDialogCancel>
                  <AlertDialogAction onClick={() => dispatch(deleteOfferById(offer.id))}>Delete</AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>
          </div>


        </div>
      </div>
      <Separator className='mb-1 mt-2' />
      <div className='flex flex-row justify-end'>
        <span className='text-sm text-gray-400'>Offer ID: {offer.id}</span>
      </div>
    </div>
  )
}

export default OfferCardManager
