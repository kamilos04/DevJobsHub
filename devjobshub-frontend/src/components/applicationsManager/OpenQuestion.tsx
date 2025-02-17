import { QuestionAndAnswer } from '@/types/questionAndAnswer'
import React from 'react'

export const OpenQuestion = ({question}: {question: QuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold'>{question.number+1}. {question.question}</p>
      <p>{question.answer}</p>
    </div>
  )
}
