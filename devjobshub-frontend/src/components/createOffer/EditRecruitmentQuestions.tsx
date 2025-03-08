import { Separator } from '../ui/separator'
import CreateQuestion from './CreateQuestion'


import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"
import { Badge } from '../ui/badge'
import { Button } from '../ui/button'
import { QuestionWithType } from '@/types/questionWithType'

const EditRecruitmentQuestions = (props: any) => {

  const handleRemoveClick = (index: number) => {
    props.setQuestionsList((prevQuestions: Array<QuestionWithType>) => {
      return prevQuestions.filter((_question: QuestionWithType, i: number) => i !== index);
    });
  }



  return (
    <div className='flex flex-col space-y-3 p-4 border-[1px] rounded-lg text-md bg-my-card'>
      <p className='font-bold'>Recruitment questions</p>

      <Separator />
      <div className='flex flex-row space-x-4'>
        <CreateQuestion questionsList={props.questionsList} setQuestionsList={props.setQuestionsList}/>
        <Accordion type="single" collapsible className="w-full">
          {props.questionsList?.map((q: any, index: number) => (
            <AccordionItem value={index.toString()} key={index}>
              <AccordionTrigger>{q.question}</AccordionTrigger>
              <AccordionContent className='flex flex-row justify-between items-end'>
                <div className='flex flex-col'>
                  <Badge className='w-min text-nowrap mb-4'>{q.type === "question" && "Open question"}{q.type === "radioQuestion" && "One-choice question"}{q.type === "multipleChoiceQuestion" && "Multiple-choice question"}</Badge>
                  {q.possibleAnswers && <><p className='mb-2'>Possible answers:</p>
                    <ul className='list-disc pl-5 break-words space-y-2'>
                      {q.possibleAnswers?.map((element: string, i: number) => (
                        <li key={i}>
                          <div className='flex flex-row space-x-2 items-center justify-between'>
                            <span className='text-sm'>{element}</span>
                          </div>
                        </li>
                      ))}
                    </ul></>}
                </div>

                <Button variant={'destructive'} onClick={() => handleRemoveClick(index)} type='button'>Delete</Button>
              </AccordionContent>
            </AccordionItem>
          ))}
        </Accordion>

      </div>




    </div>
  )
}

export default EditRecruitmentQuestions
