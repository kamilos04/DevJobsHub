import React from 'react'
import { Separator } from '../ui/separator'
import CreateQuestion from './CreateQuestion'

const EditRecruitmentQuestions = () => {
  return (
    <div className='flex flex-col space-y-3 p-3 border-[1px] rounded-lg w-96 text-md'>
            <p className='font-bold'>Recruitment questions</p>

            <Separator />
            <CreateQuestion/>
            


        </div>
  )
}

export default EditRecruitmentQuestions
