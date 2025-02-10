import { Offer } from '@/types/offer'
import React, { useEffect } from 'react'
import { MdOutlineWorkOutline } from "react-icons/md";
import { IoLocationOutline } from "react-icons/io5";
import { FaRegBuilding } from "react-icons/fa";
import { Technology } from '@/types/technology';
import { Badge } from '../ui/badge';
import { Separator } from '../ui/separator';
import { MdFavoriteBorder } from "react-icons/md";
import { RiStairsLine } from "react-icons/ri";
import { off } from 'process';
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import { likeOfferById, removeLikeOfferById } from '@/state/offer/action';

const SmallOfferCard = ({ offer }: { offer: Offer }) => {
  const navigate = useNavigate()
  const dispatch = useDispatch<any>()
  const [isLiked, setIsLiked] = React.useState<boolean>(false)
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

    const handleLikeClick = (event: any) => {
            event.stopPropagation()
            if (isLiked === false) {
                dispatch(likeOfferById(offer?.id))
                setIsLiked(true)
            }
            else if (isLiked === true) {
                dispatch(removeLikeOfferById(offer?.id))
                setIsLiked(false)
            }
        }


    useEffect(() => {
      if(offer && offer.isLiked !== null) {
          setIsLiked(offer.isLiked)
      }
    }, [offer])


  return (
    <div className='flex flex-col bg-my-card rounded-md border-[1px] pl-4 pr-4 pt-3 pb-1 cursor-pointer w-full' onClick={() => navigate(`/offer/${offer.id}`)}>
      <div className='flex flex-row justify-between'>
        <div>
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


        </div>
        <div className='flex flex-row space-x-2'>
          <MdFavoriteBorder className={`text-2xl cursor-pointer ${isLiked && "text-pink-700"}`} onClick={handleLikeClick}/>
          
        </div>

      </div>
      <div className='flex flex-row items-start flex-wrap mt-2 w-full gap-x-2 gap-y-2 mb-2'>
        {offer.requiredTechnologies.map((element: Technology) => <Badge key={element.id} variant={'secondary'} className='text-sm'>{element.name}</Badge>)}
      </div>
      <Separator className='mb-1 mt-1' />
      <div className='flex flex-row justify-end'>
        <span className='text-sm text-gray-400'>Published: {offer.dateTimeOfCreation.slice(0, -3)}</span>
      </div>

    </div>
  )
}

export default SmallOfferCard
