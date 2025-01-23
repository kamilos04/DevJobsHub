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
                            <Input type="text" id="title" placeholder="Example title" />
                        </div>
                        <div className="grid w-full max-w-sm items-center gap-2">
                            <Label htmlFor="localization">Company address: city</Label>
                            <Input type="text" id="localization" placeholder="Example city" />
                        </div>
                        <div className="grid w-full max-w-sm items-center gap-2">
                            <Label htmlFor="address">Company address: street name and number</Label>
                            <Input type="text" id="address" placeholder="Example address" />
                        </div>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="joblevel">Job level</Label>
                            <Select >
                                <SelectTrigger className="w-[15rem]" id="joblevel">
                                    <SelectValue placeholder="Select a job level" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Job levels</SelectLabel>
                                        <SelectItem value="TRAINEE">Trainee</SelectItem>
                                        <SelectItem value="JUNIOR">Junior</SelectItem>
                                        <SelectItem value="MID">Mid</SelectItem>
                                        <SelectItem value="SENIOR">Senior</SelectItem>
                                        <SelectItem value="MANAGER">Manager</SelectItem>
                                        <SelectItem value="CLEVEL">C-Level</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="operatingmode">Job level</Label>
                            <Select >
                                <SelectTrigger className="w-[15rem]" id="operatingmode">
                                    <SelectValue placeholder="Select an operating mode" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Operating modes</SelectLabel>
                                        <SelectItem value="REMOTE">Remote</SelectItem>
                                        <SelectItem value="HYBRID">Hybrid</SelectItem>
                                        <SelectItem value="STATIONARY">Stationary</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="specialization">Specialization</Label>
                            <Select >
                                <SelectTrigger className="w-[15rem]" id="specialization">
                                    <SelectValue placeholder="Select a specialization" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Specializations</SelectLabel>
                                        <SelectItem value="BACKEND">Backend</SelectItem>
                                        <SelectItem value="FRONTEND">Frontend</SelectItem>
                                        <SelectItem value="FULLSTACK">Fullstack</SelectItem>
                                        <SelectItem value="MOBILE">Mobile</SelectItem>
                                        <SelectItem value="EMBEDDED">Embedded</SelectItem>
                                        <SelectItem value="ARCHITECTURE">Architecture</SelectItem>
                                        <SelectItem value="TESTING">Testing</SelectItem>
                                        <SelectItem value="SECURITY">Security</SelectItem>
                                        <SelectItem value="AIML">AI/ML</SelectItem>
                                        <SelectItem value="DATASCIENCE">Data Science</SelectItem>
                                        <SelectItem value="HELPDESK">Helpdesk</SelectItem>
                                        <SelectItem value="UXUI">UX/UI</SelectItem>
                                        <SelectItem value="ADMINISTRATOR">Administrator</SelectItem>
                                        <SelectItem value="PROJECTMANAGER">Project Manager</SelectItem>
                                        <SelectItem value="OTHER">Other</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                        </div>

                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="expirationTime">Offer expiration date</Label>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant={"outline"}
                                        className={cn(
                                            "w-[280px] justify-start text-left font-normal",
                                            !expirationDate && "text-muted-foreground"
                                        )}
                                    >
                                        <CalendarIcon />
                                        {expirationDate ? format(expirationDate, "PPP") : <span>Pick a date</span>}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        mode="single"
                                        selected={expirationDate}
                                        onSelect={setExpirationDate}
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>

                        </div>
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

                                <ContractType isContractCheckbox={{ registerAs: "isUoP", label: "Umowa o pracę" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryUoP" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourly" }}
                                    inputMinSalary={{ props: { ...registerCreateOffer("minSalaryUoP") } }}
                                    inputMaxSalary={{ props: { ...registerCreateOffer("maxSalaryUoP") } }}
                                    control={controlCreateOffer}
                                />
                                {/* <ContractType isContractCheckboxProps={{ id: "isB2B", label: "B2B" }} showSalaryCheckBoxProps={{ id: "showSalaryB2B" }} />
                                <ContractType isContractCheckboxProps={{ id: "isUZ", label: "Umowa zlecenia" }} showSalaryCheckBoxProps={{ id: "showSalaryUZ" }} /> */}




                            </div>
                        </div>



                    </div>
                    <Button type='submit'>Register</Button>
                </form>
            </div>
        </div>
    )
}

export default CreateOffer
