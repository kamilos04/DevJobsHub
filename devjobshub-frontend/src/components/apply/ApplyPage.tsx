import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { useLocation, useNavigate, useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { getOfferById } from '@/state/offer/action';
import { MdOutlineDateRange, MdOutlineWorkOutline } from 'react-icons/md';
import { RiStairsLine } from 'react-icons/ri';
import { jobLevelsAndLabels, operatingModesAndLabels, specializationsAndLabels } from '@/constants';
import { BsPersonWorkspace } from 'react-icons/bs';
import { FaRegBuilding } from 'react-icons/fa';
import { TiDocumentText } from 'react-icons/ti';
import { IoLocationOutline } from 'react-icons/io5';
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
import { setFailNull, setSuccessNull } from '@/state/application/applicationSlice';
import { setFailNull as setFailNullFiles, setSuccessNull as setSuccessNullFiles } from '@/state/files/filesSlice';
import { Label } from '../ui/label';
import { Input } from '../ui/input';
import { getPresignedUrlForCv, uploadFileWithPresignedUrl } from '@/state/files/action';
import { setPresignedUrlCvNull } from '@/state/files/filesSlice';
import { useProfile } from '../profile/useProfile';
import { IoMdArrowRoundBack } from 'react-icons/io';

const ApplyPage = () => {
    const { id } = useParams();
    const dispatch = useDispatch<any>()
    const storeOffer = useSelector((store: any) => (store.offer))
    const storeApplication = useSelector((store: any) => (store.application))
    const filesStore = useSelector((store: any) => (store.files))
    const [questionsList, setQuestionsList] = React.useState<Array<QuestionAndAnswerWithType>>([])
    const { toast } = useToast()
    const { getProfile } = useProfile(true, false, false)
    const [cvFile, setCvFile] = React.useState<File | null>(null)
    const location = useLocation()
    const navigate = useNavigate()
    const [applyButtonDisabled, setApplyButtonDisabled] = React.useState<boolean>(false)

    useEffect(() => {
        getProfile()
        dispatch(getOfferById(Number(id)))
        dispatch(setPresignedUrlCvNull())
    }, [location.pathname])


    useEffect(() => {
        if (cvFile && storeOffer.offer) {
            if (cvFile.size < 1024 * 1024 * 5) {
                dispatch(getPresignedUrlForCv({ offerId: storeOffer.offer.id, fileExtension: cvFile.name.split(".").pop() || "" }))
            }
            else {
                setCvFile(null)
                toast({
                    variant: "destructive",
                    title: "File size is too large!"
                });

            }
        }
    }, [cvFile])


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
            setApplyButtonDisabled(false)
            dispatch(setFailNull())
        }

    }, [storeApplication.fail])


    useEffect(() => {
        if (storeApplication.success === "applyForOfferById") {
            toast({
                variant: "default",
                className: "bg-green-800",
                title: "The application has been sent.",
            });
            dispatch(setSuccessNull())
            setApplyButtonDisabled(false)
            navigate(`/offer/${storeOffer.offer.id}`)
        }

    }, [storeApplication.success])


    useEffect(() => {
        if (filesStore.success === "uploadFileWithPresignedUrl") {
            let request = {...emptyApplyRequest}
            request.cvUrl = filesStore.presignedUrlCv.key
            request.questionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "question").map((el: QuestionAndAnswerWithType) => el.question as QuestionAndAnswer)
            request.radioQuestionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "radioQuestion").map((el: QuestionAndAnswerWithType) => el.question as RadioQuestionAndAnswer)
            request.multipleChoiceQuestionsAndAnswers = questionsList.filter((element: QuestionAndAnswerWithType) => element.type === "multipleChoiceQuestion").map((el: QuestionAndAnswerWithType) => el.question as MultipleChoiceQuestionAndAnswer)
            dispatch(applyForOfferById({ id: storeOffer.offer.id, request: request }))
            dispatch(setSuccessNullFiles())
        }
        else if (filesStore.fail === "uploadFileWithPresignedUrl") {
            toast({
                variant: "destructive",
                title: "An error occurred while uploading the file!",
            });
            setApplyButtonDisabled(false)
            dispatch(setFailNullFiles())
        }
    }, [filesStore.success, filesStore.fail])


    const handleApplyClick = () => {
        if (cvFile) {
            if (filesStore.presignedUrlCv) {
                dispatch(uploadFileWithPresignedUrl({ presignedUrl: filesStore.presignedUrlCv.url, file: cvFile }))
                setApplyButtonDisabled(true)
            }
            else {
                toast({
                    variant: "destructive",
                    title: "An error occurred while uploading the file!",
                    description: "Make sure you haven't applied before and you are not using recruiter account."
                });
            }


        }
        else {
            toast({
                variant: "destructive",
                title: "You have to attach CV!",
            });
        }

    }


    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>
                {storeOffer.offer && <div className='flex flex-col mt-8 bg-my-card p-8 rounded-xl border-[1px] w-[60rem] items-center'>
                    <div className='flex flex-row items-start w-full mb-2'>
                        <Button type='button' className='flex flex-row gap-x-1' onClick={() => navigate(`/offer/${storeOffer.offer.id}`)}><IoMdArrowRoundBack />Job offer</Button>
                    </div>
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
                                {contractsStringFromOffer(storeOffer.offer) && <div className='flex flex-row mt-1 mb-2 items-center gap-x-2 text-gray-300'>
                                    <div className='p-4 bg-slate-800 rounded-2xl  border-[1px] border-blue-500'>
                                        <TiDocumentText className='text-xl text-white' />
                                    </div>
                                    <span>{contractsStringFromOffer(storeOffer.offer)}</span>
                                </div>}

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
                    <div className='flex flex-row w-full justify-start mt-16 mb-12'>
                        <div className="grid w-full max-w-sm items-center gap-1.5">
                            <Label htmlFor="cv" className='text-base'>Attach your CV</Label>
                            <Input id="cv" type="file" onChange={(e) => {
                                const fileList: FileList | null = e.target.files
                                if (fileList && fileList.length > 0) {
                                    setCvFile(fileList[0])
                                }
                            }} />
                        </div>
                    </div>
                    <div className='w-full flex flex-col gap-y-10'>
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
                        <Button disabled={applyButtonDisabled} className='w-32' onClick={() => handleApplyClick()}>Apply</Button>
                    </div>


                </div>}

            </div>
        </div>
    )
}

export default ApplyPage
