import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { format } from "date-fns"
import { CalendarIcon } from "lucide-react"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import TechnologiesCombobox from './TechnologiesCombobox'
import { Textarea } from "@/components/ui/textarea"
import { Checkbox } from "@/components/ui/checkbox"
import { Separator } from "@/components/ui/separator"
import ContractType from './ContractType'
import { Controller, useForm } from 'react-hook-form'
import { watch } from 'fs'
import SelectJobLevel from './SelectJobLevel'
import SelectSpecialization from './SelectSpecialization'
import SelectOperatingMode from './SelectOperatingMode'
import ExpirationDatePicker from './ExpirationDatePicker'
import SelectTechnologiesDialog from './SelectTechnologiesDialog'
import { Technology } from '@/types/technology'
import EditBulletPoints from './EditBulletPoints'
import { Question } from '@/types/question'
import { RadioQuestion } from '@/types/question'
import { MultipleChoiceQuestion } from '@/types/multipleChoiceQuestion'
import CreateQuestion from './CreateQuestion'
import EditRecruitmentQuestions from './EditRecruitmentQuestions'
import { QuestionWithType } from '@/types/questionWithType'



const CreateOffer = () => {
    const [expirationDate, setExpirationDate] = React.useState<Date>()
    const {
        register: registerCreateOffer,
        handleSubmit: handleCreateOffer,
        setValue: setValueCreateOffer,
        control: controlCreateOffer,
        formState: { errors: createOfferErrors }
    } = useForm({
    })

    const [requiredTechnologies, setRequiredTechnologies] = React.useState<Array<Technology>>([])
    const [niceToHaveTechnologies, setNiceToHaveTechnologies] = React.useState<Array<Technology>>([])
    const [responsibilities, setResponsibilities] = React.useState<Array<string>>([])
    const [requirements, setRequirements] = React.useState<Array<string>>([])
    const [niceToHave, setNiceToHave] = React.useState<Array<string>>([])
    const [whatWeOffer, setWhatWeOffer] = React.useState<Array<string>>([])
    const [questions, setQuestions] = React.useState<Array<Question>>([])
    const [radioQuestions, setRadioQuestions] = React.useState<Array<RadioQuestion>>([])
    const [multipleChoiceQuestions, setMultipleChoiceQuestions] = React.useState<Array<MultipleChoiceQuestion>>([])
    const [questionsList, setQuestionsList] = React.useState<Array<QuestionWithType>>([])


    const onCreateOfferSubmit = (data: any) => {
        console.log(data)
    }

    // const handleExpirationDateChange = (data) => {

    // }


    return (
        <div className='flex flex-col'>
            <Navbar />
            <div className='flex flex-col items-center'>

                <form onSubmit={handleCreateOffer(onCreateOfferSubmit)}>
                    <div className='border-[1px] w-min mt-5 rounded-lg p-8'>
                        <div className='flex flex-row justify-center mb-3'>
                            <h1 className='text-xl font-bold'>Create new job offer</h1>
                        </div>
                        <Separator />
                        <div className='flex flex-row flex-wrap space-x-8 mt-6'>
                            <div className='flex flex-col space-y-6'>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="title">Title</Label>
                                    <Input type="text" id="title" placeholder="Example title" {...registerCreateOffer("name")} />
                                </div>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="localization">Company address: city</Label>
                                    <Input type="text" id="localization" placeholder="Warszawa" {...registerCreateOffer("localization")} />
                                </div>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="address">Company address: street name and building number</Label>
                                    <Input type="text" id="address" placeholder="Kolejowa 14/20" {...registerCreateOffer("address")} />
                                </div>
                            </div>


                            <div className='flex flex-col rounded-lg bg-my-card p-4 w-min border-[1px] space-y-6'>
                                <SelectJobLevel control={controlCreateOffer} />
                                <SelectOperatingMode control={controlCreateOffer} />
                                <SelectSpecialization control={controlCreateOffer} />
                            </div>


                            <div className='flex flex-col rounded-lg bg-my-card p-4 w-min h-min border-[1px] space-y-6'>
                                <ExpirationDatePicker control={controlCreateOffer} />
                                <div className='flex flex-col space-y-2'>
                                    <Label htmlFor="expirationTime">Offer expiration time</Label>
                                    <input type='time' className='text-md bg-background p-1 pl-3 border-[1px] rounded-lg w-[6rem]' id="expirationTime"></input>
                                </div>
                            </div>

                        </div>









                        {/* <div>
                            <TechnologiesCombobox />
                        </div> */}
                        <div className='flex flex-row w-full space-x-8 mt-12'>
                            <div className='flex flex-col space-y-6 w-full'>
                                <div className="grid w-full gap-2">
                                    <Label htmlFor="responsibilitiesText">Responsibilities</Label>
                                    <Textarea id="responsibilitiesText" />
                                </div>
                                <div className="grid w-full gap-2">
                                    <Label htmlFor="aboutProject">About project</Label>
                                    <Textarea id="aboutProject" {...registerCreateOffer('aboutProject')} />
                                </div>
                            </div>

                            <div className='flex flex-col space-y-8 mb-8 mt-4 justify-center'>
                                <SelectTechnologiesDialog technologies={requiredTechnologies} setTechnologies={setRequiredTechnologies} text="Change required technologies" />
                                <SelectTechnologiesDialog technologies={niceToHaveTechnologies} setTechnologies={setNiceToHaveTechnologies} text="Change optional technologies" />
                            </div>
                        </div>





                        <div className='flex flex-col space-y-3 mt-16 mb-16'>
                            <p className='font-bold'>What types of contracts do you offer?</p>
                            <div className='flex flex-row space-x-4'>

                                <ContractType isContractCheckbox={{ registerAs: "isUoP", label: "Employment contract" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryUoP" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyUoP" }}
                                    inputMinSalary={{ props: { ...registerCreateOffer("minSalaryUoP") } }}
                                    inputMaxSalary={{ props: { ...registerCreateOffer("maxSalaryUoP") } }}
                                    control={controlCreateOffer}
                                />
                                <ContractType isContractCheckbox={{ registerAs: "isB2B", label: "B2B" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryB2B" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyB2B" }}
                                    inputMinSalary={{ props: { ...registerCreateOffer("minSalaryB2B") } }}
                                    inputMaxSalary={{ props: { ...registerCreateOffer("maxSalaryB2B") } }}
                                    control={controlCreateOffer}
                                />
                                <ContractType isContractCheckbox={{ registerAs: "isUZ", label: "Order contract" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryUZ" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyUZ" }}
                                    inputMinSalary={{ props: { ...registerCreateOffer("minSalaryUZ") } }}
                                    inputMaxSalary={{ props: { ...registerCreateOffer("maxSalaryUZ") } }}
                                    control={controlCreateOffer}
                                />




                            </div>
                        </div>
                        <div className='flex flex-col'>
                            <p className='font-bold'>Bullet points</p>
                            <div className='flex flex-col flex-wrap w-full mb-16 mt-3'>
                                <div className='flex flex-row w-full mb-4'>
                                    <div className='mr-4 w-full'>
                                        <EditBulletPoints list={responsibilities} setList={setResponsibilities} text={"Responsibilities"} />
                                    </div>
                                    <div className='w-full'>
                                        <EditBulletPoints list={requirements} setList={setRequirements} text={"Requirements"} />
                                    </div>
                                </div>
                                <div className='flex flex-row w-full'>
                                    <div className='mr-4 w-full'>
                                        <EditBulletPoints list={niceToHave} setList={setNiceToHave} text={"Nice to have"} />
                                    </div>
                                    <div className='w-full'>
                                        <EditBulletPoints list={whatWeOffer} setList={setWhatWeOffer} text={"What we offer"} />
                                    </div>
                                </div>

                            </div>
                        </div>





                        <EditRecruitmentQuestions questionsList={questionsList} setQuestionsList={setQuestionsList} />
                        
                        <div className='flex flex-row justify-end mt-10'>
                            <Button type='submit'>Create offer</Button>
                        </div>
                        
                    </div>
                    
                </form>
            </div>
        </div>
    )
}

export default CreateOffer
