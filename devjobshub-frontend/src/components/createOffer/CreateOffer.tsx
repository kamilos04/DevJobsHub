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
import {RadioQuestion} from '@/types/question'
import { MultipleChoiceQuestion } from '@/types/multipleChoiceQuestion'
import CreateQuestion from './CreateQuestion'
import EditRecruitmentQuestions from './EditRecruitmentQuestions'



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
                    <div className='border-[1px] w-min mt-5 rounded-lg p-8 space-y-6'>
                        <div className="grid w-full max-w-sm items-center gap-2">
                            <Label htmlFor="title">Title</Label>
                            <Input type="text" id="title" placeholder="Example title" {...registerCreateOffer("name")} />
                        </div>
                        <div className="grid w-full max-w-sm items-center gap-2">
                            <Label htmlFor="localization">Company address: city</Label>
                            <Input type="text" id="localization" placeholder="Example city" {...registerCreateOffer("localization")} />
                        </div>
                        <div className="grid w-full max-w-sm items-center gap-2">
                            <Label htmlFor="address">Company address: street name and number</Label>
                            <Input type="text" id="address" placeholder="Example address" {...registerCreateOffer("address")} />
                        </div>
                        <SelectJobLevel control={controlCreateOffer} />
                        <SelectOperatingMode control={controlCreateOffer} />
                        <SelectSpecialization control={controlCreateOffer} />

                        <ExpirationDatePicker control={controlCreateOffer} />
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="expirationTime">Offer expiration time</Label>
                            <input type='time' className='text-md bg-background p-1 pl-3 border-[1px] rounded-lg w-[6rem]' id="expirationTime"></input>
                        </div>
                        <div>
                            <TechnologiesCombobox />
                        </div>
                        <div className="grid w-full gap-2">
                            <Label htmlFor="responsibilitiesText">Responsibilities</Label>
                            <Textarea id="responsibilitiesText" />
                        </div>
                        <div className="grid w-full gap-2">
                            <Label htmlFor="aboutProject">About project</Label>
                            <Textarea id="aboutProject" {...registerCreateOffer('aboutProject')} />
                        </div>
                        <div className='flex flex-col space-y-3'>
                            <p>What types of contracts do you offer?</p>
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
                        <SelectTechnologiesDialog technologies={requiredTechnologies} setTechnologies={setRequiredTechnologies} text="Change required technologies" />
                        <SelectTechnologiesDialog technologies={niceToHaveTechnologies} setTechnologies={setNiceToHaveTechnologies} text="Change optional technologies" />

                        <EditBulletPoints list={responsibilities} setList={setResponsibilities} text={"Responsibilities"}/>
                        <EditBulletPoints list={requirements} setList={setRequirements} text={"Requirements"}/>
                        <EditBulletPoints list={niceToHave} setList={setNiceToHave} text={"Nice to have"}/>
                        <EditBulletPoints list={whatWeOffer} setList={setWhatWeOffer} text={"Requirements"}/>
                        <EditRecruitmentQuestions/>
                    </div>
                    <Button type='submit'>Register</Button>
                </form>
            </div>
        </div>
    )
}

export default CreateOffer
