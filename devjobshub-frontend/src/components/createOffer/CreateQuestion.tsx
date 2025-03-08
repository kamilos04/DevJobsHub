import React, { useState } from 'react'
import { Separator } from '../ui/separator'
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Input } from '../ui/input'
import { Label } from '../ui/label'
import { Button } from '../ui/button'
import { QuestionWithType } from '@/types/questionWithType'

const CreateQuestion = (props: any) => {
    const [questionType, setQuestionType] = useState("")
    const [questionValue, setQuestionValue] = useState("")
    const [answers, setAnswers] = React.useState<Array<string>>([])
    const [newAnswer, setNewAnswer] = useState("")

    const handleAddAnswerClick = () => {
        if (newAnswer.length > 0) {
            setAnswers([...answers, newAnswer])
            setNewAnswer("")
        }
    }

    const removeAnswer = (index: number) => {
        setAnswers(answers.filter((_: string, i: number) => i !== index));
    }

    const resetForm = () => {
        setQuestionType("")
        setQuestionValue("")
        setAnswers([])
        setNewAnswer("")
    }

    const handleAddQuestionClick = () => {
        if (questionType!=="" && questionValue !== ""){
            if(questionType=="question"){
                const q: QuestionWithType = {
                    question: questionValue,
                    type: 'question',
                    possibleAnswers: null
                }
                props.setQuestionsList([...props.questionsList, q])
                resetForm()
            }

            if(questionType=="radioQuestion" && answers.length !== 0){
                const q: QuestionWithType = {
                    question: questionValue,
                    type: 'radioQuestion',
                    possibleAnswers: answers
                }
                props.setQuestionsList([...props.questionsList, q])
                resetForm()
            }

            if(questionType=="multipleChoiceQuestion" && answers.length !== 0){
                const q: QuestionWithType = {
                    question: questionValue,
                    type: 'multipleChoiceQuestion',
                    possibleAnswers: answers
                }
                props.setQuestionsList([...props.questionsList, q])
                resetForm()
            }


        }
    }

    return (
        <div className='flex flex-col space-y-3 p-3 border-[1px] rounded-lg w-[35rem] h-min'>
            <p className='text-md'>Create question</p>
            <Separator />
            <div className='flex flex-col space-y-2'>
                <Label htmlFor="selectQuestionType">Question type</Label>
                <Select value={questionType} onValueChange={(data) => setQuestionType(data)}>
                    <SelectTrigger className="" id="selectQuestionType">
                        <SelectValue placeholder="Select question type" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value="question">Open question</SelectItem>
                            <SelectItem value="radioQuestion">One-choice question</SelectItem>
                            <SelectItem value="multipleChoiceQuestion">Multiple-choice question</SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
            <div className='flex flex-col space-y-2 pt-2'>
                <Label htmlFor="newQuestion">Question</Label>
                <Input type="text" placeholder="Enter your question" id="newQuestion" value={questionValue} onChange={(e) => setQuestionValue(e.target.value)} onKeyDown={(event) => {if (event.key === "Enter") {event.preventDefault();}}}/>
            </div>
            {(questionType === "radioQuestion" || questionType === "multipleChoiceQuestion") &&

                <div className='flex flex-col space-y-2'>
                    <div className='flex flex-col space-y-2 pt-5'>
                        <Label htmlFor="addAnswerInput">Add answer</Label>
                        <div className='flex flex-row space-x-3'>
                            <Input type="text" placeholder="Enter a possible answer" id="addAnswerInput" value={newAnswer} onChange={(e) => setNewAnswer(e.target.value)} onKeyDown={(event) => {if (event.key === "Enter") {event.preventDefault();}}}/>
                            <Button onClick={() => handleAddAnswerClick()}  type='button'>Add answer</Button>
                        </div>


                    </div>
                    <div className='flex flex-col'>
                        <p className='pt-2'>Possible answers:</p>
                        <ul className='list-disc pl-5 space-y-2 text-gray-300'>
                            {answers.map((element: string, index: number) => (
                                <li key={index}>
                                    <div className='flex flex-row space-x-2 items-center justify-between'>
                                        <span className='text-sm break-words'>{element}</span>
                                        <Button className='pt-2 h-[1.5rem] text-xs' variant={"secondary"} onClick={() => removeAnswer(index)}  type='button'>Remove</Button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>}
                <div className='pt-3 w-full'>
                    <Button className='w-full' onClick={() => handleAddQuestionClick() } type='button'>Add question</Button>
                </div>
                


        </div>
    )
}

export default CreateQuestion
