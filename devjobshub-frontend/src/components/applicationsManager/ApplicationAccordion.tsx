import React from 'react'
import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "@/components/ui/accordion"
import { Application } from '@/types/application'
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType'
import { addMultipleChoiceQuestionsToQuestionsList, addMultipleChoiceQuestionsWithAnswerToQuestionsList, addOpenQuestionsToQuestionsList, addOpenQuestionsWithAnswerToQuestionsList, addRadioQuestionsToQuestionsList, addRadioQuestionsWithAnswerToQuestionsList } from '@/utils/questionsUtils'
import { OpenQuestion } from './OpenQuestion'
import { QuestionAndAnswer } from '@/types/questionAndAnswer'
import { RadioQuestion } from './RadioQuestion'
import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer'
import { MultipleChoiceQuestion } from './MultipleChoiceQuestion'
import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'
import { Button } from '../ui/button'
import { useDispatch } from 'react-redux'
import { setApplicationStatus } from '@/state/application/action'

export const ApplicationAccordion = ({ application }: { application: Application }) => {
    const dispatch = useDispatch<any>()
    let qlist: Array<QuestionAndAnswerWithType> = []
    addOpenQuestionsWithAnswerToQuestionsList(application.questionsAndAnswers, qlist)
    addRadioQuestionsWithAnswerToQuestionsList(application.radioQuestionsAndAnswers, qlist)
    addMultipleChoiceQuestionsWithAnswerToQuestionsList(application.multipleChoiceQuestionsAndAnswers, qlist)
    const questionsList: Array<QuestionAndAnswerWithType> = [...qlist].sort((a, b) => (a.question.number - b.question.number))
    

    const handleChangeStatus = (status: string) => {
        dispatch(setApplicationStatus({id: application.id, status: status}))
    }

    return (
        <div>
            <AccordionItem value="item-1">
                <AccordionTrigger>Applied on: {application.dateTimeOfCreation.slice(0, -3)}</AccordionTrigger>
                <AccordionContent>
                    <div className='flex flex-row justify-between'>
                        <div className='flex flex-col gap-y-6'>
                            {questionsList.map((element: QuestionAndAnswerWithType) => {
                                if (element.type === "question") {
                                    return <OpenQuestion key={element.question.number} question={element.question as QuestionAndAnswer} />
                                }
                                if (element.type === "radioQuestion") {
                                    return <RadioQuestion key={element.question.number} question={element.question as RadioQuestionAndAnswer} />
                                }
                                if (element.type === "multipleChoiceQuestion") {
                                    return <MultipleChoiceQuestion key={element.question.number} question={element.question as MultipleChoiceQuestionAndAnswer} />
                                }
                            })}
                        </div>
                        <div className='flex flex-col gap-y-2 justify-end'>
                            {application.status !== "FAVOURITE" &&<Button variant={'default'} onClick={() => handleChangeStatus("FAVOURITE")}>Move to favourites</Button>}
                            {application.status !== "REJECTED" && <Button variant={'default'} onClick={() => handleChangeStatus("REJECTED")}>Move to rejected</Button>}
                            {application.status !== "NO_STATUS" &&<Button variant={'default'} onClick={() => handleChangeStatus("NO_STATUS")}>Remove status</Button>}
                        </div>
                    </div>

                </AccordionContent>
            </AccordionItem>
        </div>



    )
}
