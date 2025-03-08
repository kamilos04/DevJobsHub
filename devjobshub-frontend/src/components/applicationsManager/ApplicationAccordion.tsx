import { useEffect, useRef } from 'react'
import {
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "@/components/ui/accordion"
import { Application } from '@/types/application'
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType'
import { addMultipleChoiceQuestionsWithAnswerToQuestionsList, addOpenQuestionsWithAnswerToQuestionsList, addRadioQuestionsWithAnswerToQuestionsList } from '@/utils/questionsUtils'
import { OpenQuestion } from './OpenQuestion'
import { QuestionAndAnswer } from '@/types/questionAndAnswer'
import { RadioQuestion } from './RadioQuestion'
import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer'
import { MultipleChoiceQuestion } from './MultipleChoiceQuestion'
import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'
import { Button } from '../ui/button'
import { useDispatch, useSelector } from 'react-redux'
import { setApplicationStatus } from '@/state/application/action'
import { getPresignedUrlToDownloadCV } from '@/state/files/action'
import { setSuccessNull } from '@/state/files/filesSlice'
import { Offer } from '@/types/offer'
import { useToast } from '@/hooks/use-toast'

export const ApplicationAccordion = ({ application, offer }: { application: Application, offer: Offer }) => {
    const filesStore = useSelector((store: any) => (store.files))
    const hasDownloaded = useRef(false)
    const dispatch = useDispatch<any>()
    const {toast} = useToast()
    let qlist: Array<QuestionAndAnswerWithType> = []
    addOpenQuestionsWithAnswerToQuestionsList(application.questionsAndAnswers, qlist)
    addRadioQuestionsWithAnswerToQuestionsList(application.radioQuestionsAndAnswers, qlist)
    addMultipleChoiceQuestionsWithAnswerToQuestionsList(application.multipleChoiceQuestionsAndAnswers, qlist)
    const questionsList: Array<QuestionAndAnswerWithType> = [...qlist].sort((a, b) => (a.question.number - b.question.number))


    const handleChangeStatus = (status: string) => {
        dispatch(setApplicationStatus({ id: application.id, status: status }))
    }


    const handleClickDownloadCV = () => {
        dispatch(getPresignedUrlToDownloadCV({ applicationId: application.id }))
    }


    const downloadFile = async (fileUrl: string, filename: string) => {
        try {
            const response = await fetch(fileUrl, { method: "GET" });

            if (!response.ok) {
                throw new Error(`error`);
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", filename);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            window.URL.revokeObjectURL(url);
            hasDownloaded.current = false
        } catch (error) {
            toast({
                variant: "destructive",
                title: "An error occurred while downloading the file!"
            });
        }
    };



    useEffect(() => {
        if (filesStore.success === "getPresignedUrlToDownloadCV" && !hasDownloaded.current) {
            hasDownloaded.current = true
            dispatch(setSuccessNull())
            downloadFile(filesStore.presignedUrlCvToDownload.url, `offerID_${offer.id} applicationID_${application.id}.${filesStore.presignedUrlCvToDownload.key.split(".").pop()}`)
            
        }
    }, [filesStore.success])

    return (
        <div>
            <AccordionItem value={String(application.id)}>
                <AccordionTrigger>
                    <div className='flex flex-row space-x-8'>
                        <span>
                            Applied on: {application.dateTimeOfCreation.slice(0, -3)}
                        </span>
                        <span>Application ID: {application.id}</span>
                    </div>
                </AccordionTrigger>
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
                            <Button variant={'default'} className='bg-green-600' onClick={() => handleClickDownloadCV()}>Download CV</Button>
                            {application.status !== "FAVOURITE" && <Button variant={'default'} onClick={() => handleChangeStatus("FAVOURITE")}>Move to favourites</Button>}
                            {application.status !== "REJECTED" && <Button variant={'default'} onClick={() => handleChangeStatus("REJECTED")}>Move to rejected</Button>}
                            {application.status !== "NO_STATUS" && <Button variant={'default'} onClick={() => handleChangeStatus("NO_STATUS")}>Remove status</Button>}

                        </div>
                    </div>

                </AccordionContent>
            </AccordionItem>
        </div>



    )
}
