import React from 'react'
import { Input } from "@/components/ui/input"
import { Question } from '@/types/question'
import { QuestionAndAnswer } from '@/types/questionAndAnswer'
import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer'
import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType'

const RadioQuestion = ({ question, setQuestionsList }: { question: RadioQuestionAndAnswer, setQuestionsList: any }) => {
  const changeAnswer = (value: string) => {
      setQuestionsList((prevItems: Array<QuestionAndAnswerWithType>) =>
        prevItems.map((item) =>
          item.question.number === question.number ? {...item, question: {...question, answer: Number(value)}} : item
        )
      );
    }



  return (
    <div className='flex flex-col gap-y-2'>
      <p>{question.number + 1}. {question.question}</p>
      <RadioGroup defaultValue="comfortable" value={String(question.answer)} onValueChange={changeAnswer}>
        {question.possibleAnswers.map((element, index) => 
        <div className="flex items-center space-x-2" key={index}>
          <RadioGroupItem value={String(index)} id={`${question.number}-${index}`} />
          <Label htmlFor={`${question.number}-${index}`}>{element}</Label>
        </div>)}
        
      </RadioGroup>
    </div>
  )
}

export default RadioQuestion
