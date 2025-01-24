import React from 'react'
import { Badge } from "@/components/ui/badge"
import { Technology } from '@/types/technology'

const TechnologyBadge = (props: any) => {
  return (
    <Badge className='mb-2 mr-2 cursor-pointer text-sm' onClick={() => props.setTechnologies(props.technologies.filter((item: Technology) => item.id !== props.technology.id))}>{props.technology.name}</Badge>
  )
}

export default TechnologyBadge
