import { Offer } from '@/types/offer'
import React from 'react'

const OfferCard = ({offer}: {offer: Offer}) => {
  return (
    <div className='flex flex-col rounded-md'>
      <span>{offer.name}</span>
    </div>
  )
}

export default OfferCard
