import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { getOfferById } from '@/state/offer/action';
import { store } from '@/state/store';
import { MdOutlineDateRange, MdOutlineWorkOutline } from 'react-icons/md';
import { RiStairsLine } from 'react-icons/ri';
import { jobLevelsAndLabels, operatingModesAndLabels, specializationsAndLabels } from '@/constants';
import { BsPersonWorkspace } from 'react-icons/bs';
import { FaRegBuilding } from 'react-icons/fa';
import { TiDocumentText } from 'react-icons/ti';
import { IoLocationOutline } from 'react-icons/io5';
import { calcDaysToExpirationDateFromString, calcSecondsToExpirationDateFromString } from '@/utils/dateUtils';
import { contractsStringFromOffer, getExpirationDate } from '@/utils/utils';
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType';
import { addMultipleChoiceQuestionsToQuestionsList, addOpenQuestionsToQuestionsList, addRadioQuestionsToQuestionsList } from '@/utils/questionsUtils';
import OpenQuestion from './OpenQuestion';
import { QuestionAndAnswer } from '@/types/questionAndAnswer';
import RadioQuestion from './RadioQuestion';
import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer';
import MultipleChoiceQuestion from './MultipleChoiceQuestion';
import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer';
import { Button } from "@/components/ui/button"
import { emptyApplyRequest } from '@/types/applyRequest';
import { applyForOfferById } from '@/state/application/action';
import { useToast } from '@/hooks/use-toast'
import { setFailNull } from '@/state/application/applicationSlice';

const ApplyPage = () => {
    const { id } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => (store.offer))
    const storeApplication = useSelector((store: any) => (store.application))
    const [questionsList, setQuestionsList] = React.useState<Array<QuestionAndAnswerWithType>>([])
    const { toast } = useToast()

    useEffect(() => {
        dispatch(getOfferById(Number(id)))


    }, [id])


    useEffect(() => {
        if (storeOffer.success === "getOfferById") {
            let qlist: Array<QuestionAndAnswerWithType> = []
            addOpenQuestionsToQuestionsList(storeOffer.offer.questions, qlist)
            addRadioQuestionsToQuestionsList(storeOffer.offer.radioQuestions, qlist)
            addMultipleChoiceQuestionsToQuestionsList(storeOffer.offer.multipleChoiceQuestions, qlist)
            const sortedList = [...qlist].sort((a, b) => (a.question.number - b.question.number))
            setQuestionsList(sortedList)

        }
    }, [storeOffer.success])


    useEffect(() => {
        if (storeApplication.fail === "applyForOfferById") {
            if (storeApplication.error === "User already applied for this offer") {
                toast({
                    variant: "destructive",
                    title: "You have already applied for this job offer!"
                });

            }
            else {
                toast({
                    variant: "destructive",
                    title: "An error occurred!",
                    description: "Make sure you enter your details correctly."
                });
            }
            dispatch(setFailNull())
        }

    }, [storeApplication.fail])



    const handleApplyClick = () => {
        let request = emptyApplyRequest
        request.cvUrl = "test"
        request.questionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "question").map((el: QuestionAndAnswerWithType) => el.question as QuestionAndAnswer)
        request.radioQuestionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "radioQuestion").map((el: QuestionAndAnswerWithType) => el.question as RadioQuestionAndAnswer)
        request.multipleChoiceQuestionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "multipleChoiceQuestion").map((el: QuestionAndAnswerWithType) => el.question as MultipleChoiceQuestionAndAnswer)
        dispatch(applyForOfferById({ id: storeOffer.offer.id, request: request }))
    }


    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>
                {storeOffer.offer && <div className='flex flex-col mt-8 bg-my-card p-8 rounded-xl border-[1px] w-[60rem] items-center'>
                    <div className='flex flex-col'>
                        <p>You are applying for a position</p>
                        <p className='text-3xl font-bold'>{storeOffer.offer.name}</p>
                        <div className='flex flex-row mt-1 mb-2 items-center gap-x-1 text-gray-300'>
                            <MdOutlineWorkOutline className='text-2xl' />
                            <span className='text-lg'>{storeOffer.offer?.firmName}</span>
                        </div>
                    </div>
                    <div className=''>
                        <div className='flex flex-row gap-x-20 mt-4'>
                            <div className='flex flex-col'>
                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
                                        <RiStairsLine className='text-xl text-white' />
                                    </div>
                                    <span>{jobLevelsAndLabels.map((element: any) => {
                                        if (element.value === storeOffer.offer?.jobLevel) {
                                            return element.label
                                        }
                                    })}</span>
                                </div>
                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
                                        <BsPersonWorkspace className='text-xl text-white' />
                                    </div>
                                    <span>{specializationsAndLabels.map((element: any) => {
                                        if (element.value === storeOffer.offer?.specialization) {
                                            return element.label
                                        }
                                    })}</span>
                                </div>

                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl border-[1px] border-blue-500'>
                                        <FaRegBuilding className='text-xl text-white' />
                                    </div>
                                    <span>{operatingModesAndLabels.map((element: any) => {
                                        if (element.value === storeOffer.offer?.operatingMode) {
                                            return element.label
                                        }
                                    })}</span>
                                </div>


                            </div>

                            <div className='flex flex-col w-[20rem]'>
                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                        <TiDocumentText className='text-xl text-white' />
                                    </div>
                                    <span>{contractsStringFromOffer(storeOffer.offer)}</span>
                                </div>

                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                        <IoLocationOutline className='text-xl text-white' />
                                    </div>
                                    <div className='flex flex-col'>
                                        <span>{storeOffer.offer?.localization}</span>
                                        <span className='text-sm'>{storeOffer.offer?.address}</span>
                                    </div>

                                </div>

                                <div className='flex flex-row mt-1 mb-2 items-center gap-x-3 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                        <MdOutlineDateRange className='text-xl text-white' />
                                    </div>
                                    <div className='flex flex-col'>
                                        <span>{storeOffer.offer && getExpirationDate(storeOffer.offer)
                                        }</span>
                                        <span className='text-sm'>Expiration date: {storeOffer.offer?.expirationDate.slice(0, -3)}</span>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div className='w-full flex flex-col gap-y-10 mt-16'>
                        {questionsList.map((element: QuestionAndAnswerWithType) => {
                            if (element.type === "question") {
                                return <OpenQuestion key={element.question.number} question={element.question as QuestionAndAnswer} setQuestionsList={setQuestionsList} />
                            }
                            if (element.type === "radioQuestion") {
                                return <RadioQuestion key={element.question.number} question={element.question as RadioQuestionAndAnswer} setQuestionsList={setQuestionsList} />
                            }
                            if (element.type === "multipleChoiceQuestion") {
                                return <MultipleChoiceQuestion key={element.question.number} question={element.question as MultipleChoiceQuestionAndAnswer} setQuestionsList={setQuestionsList} />
                            }
                        })}
                    </div>
                    <div className='flex flex-row justify-center w-full mt-6'>
                        <Button className='w-32' onClick={() => handleApplyClick()}>Apply</Button>
                    </div>


                </div>}

            </div>
        </div>
    )
}

export default ApplyPage
