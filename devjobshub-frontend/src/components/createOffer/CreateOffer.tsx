import React, { useEffect, useRef } from 'react'
import Navbar from '../navbar/Navbar'
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { Separator } from "@/components/ui/separator"
import ContractType from './ContractType'
import { useForm } from 'react-hook-form'
import SelectJobLevel from './SelectJobLevel'
import SelectSpecialization from './SelectSpecialization'
import SelectOperatingMode from './SelectOperatingMode'
import ExpirationDatePicker from './ExpirationDatePicker'
import SelectTechnologiesDialog from './SelectTechnologiesDialog'
import { Technology } from '@/types/technology'
import EditBulletPoints from './EditBulletPoints'
import EditRecruitmentQuestions from './EditRecruitmentQuestions'
import { QuestionWithType } from '@/types/questionWithType'
import { emptyCreateOfferRequest } from '@/types/createOfferRequest'
import { formatExpirationDate } from '@/utils/dateUtils'
import { convertQuestionsListToListOfMultipleChoiceQuestions, convertQuestionsListToListOfOpenQuestions, convertQuestionsListToListOfRadioQuestions } from '@/utils/questionsUtils'
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup'
import { useDispatch, useSelector } from 'react-redux'
import { createOffer } from '@/state/offer/action'
import { useLocation, useNavigate } from 'react-router'
import { useProfile } from '../profile/useProfile'
import { useToast } from '@/hooks/use-toast'
import { setFailNull, setSuccessNull } from '@/state/offer/offerSlice'
import { IoMdArrowRoundBack } from 'react-icons/io'
import { getPresignedUrlForCompanyImage, uploadFileWithPresignedUrl } from '@/state/files/action'
import { resetFilesStore, setFailNull as setFailNullFiles, setSuccessNull as setSuccessNullFiles } from '@/state/files/filesSlice';


const CreateOffer = () => {
    const dispatch = useDispatch<any>()
    const offerStore = useSelector((store: any) => (store.offer))
    const { getProfile, profileStore } = useProfile(true, true, false)
    const { toast } = useToast()
    const navigate = useNavigate()
    const location = useLocation()
    const [cvFile, setCvFile] = React.useState<File | null>(null)
    const [createButtonDisabled, setCreateButtonDisabled] = React.useState<boolean>(false)
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const filesStore = useSelector((store: any) => (store.files))
    const [req, setReq] = React.useState<any>(null)

    useEffect(() => {
        dispatch(resetFilesStore())
        getProfile()
    }, [location.pathname])




    useEffect(() => {
        if (offerStore.success === "createOffer") {
            toast({
                variant: "default",
                className: "bg-green-800",
                title: "The offer has been created.",
            });
            dispatch(setSuccessNull())
            navigate("/recruiter/manager")
        }
    }, [offerStore.success])


    useEffect(() => {
        if (offerStore.fail === "createOffer") {
            toast({
                variant: "destructive",
                title: "An error occurred!",
                description: "Make sure you enter your details correctly."
            });

            dispatch(setFailNull())
            setCreateButtonDisabled(false)
        }
    }, [offerStore.fail])



    useEffect(() => {
        if (cvFile) {
            if (cvFile.size < 1024 * 1024 * 5) {
                dispatch(getPresignedUrlForCompanyImage({ fileExtension: cvFile.name.split(".").pop() || "" }))
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


    const createOfferSchema = yup.object().shape({
        aboutProject: yup.string(),
        address: yup.string()
            .required('Address is required'),

        expirationDate: yup.date()
            .required('Expiration date is required')
            .min(new Date(), 'The expiration date must be after today'),

        expirationTime: yup.string()
            .required('Expiration time is required'),

        isB2B: yup.boolean(),

        isUZ: yup.boolean(),

        isUoP: yup.boolean(),

        jobLevel: yup.string()
            .required('Job level is required'),

        localization: yup.string()
            .required('Localization is required'),

        maxSalaryB2B: yup.number().when('showSalaryB2B', ([showSalaryB2B], schema) => {
            return showSalaryB2B ? schema.required("Field is required") : schema.nullable()
        }),

        maxSalaryUZ: yup.number().when('showSalaryUZ', ([showSalaryUZ], schema) => {
            return showSalaryUZ ? schema.required("Field is required") : schema.nullable()
        }),

        maxSalaryUoP: yup.number().when('showSalaryUoP', ([showSalaryUoP], schema) => {
            return showSalaryUoP ? schema.required("Field is required") : schema.nullable()
        }),

        minSalaryB2B: yup.number().when('showSalaryB2B', ([showSalaryB2B], schema) => {
            return showSalaryB2B ? schema.required("Field is required") : schema.nullable()
        }),

        minSalaryUZ: yup.number().when('showSalaryUZ', ([showSalaryUZ], schema) => {
            return showSalaryUZ ? schema.required("Field is required") : schema.nullable()
        }),

        minSalaryUoP: yup.number().when('showSalaryUoP', ([showSalaryUoP], schema) => {
            return showSalaryUoP ? schema.required("Field is required") : schema.nullable()
        }),

        // monthlyOrHourlyB2B: yup.string(),

        monthlyOrHourlyB2B: yup.string().when('showSalaryB2B', ([showSalaryB2B], schema) => {
            return showSalaryB2B ? schema.required("Field is required") : schema
        }),

        monthlyOrHourlyUoP: yup.string().when('showSalaryUoP', ([showSalaryUoP], schema) => {
            return showSalaryUoP ? schema.required("Field is required") : schema
        }),

        monthlyOrHourlyUZ: yup.string().when('showSalaryUZ', ([showSalaryUZ], schema) => {
            return showSalaryUZ ? schema.required("Field is required") : schema
        }),

        name: yup.string()
            .required('Offer title is required'),

        firmName: yup.string().required('Company name is required'),

        operatingMode: yup.string()
            .required('Operating mode is required'),

        responsibilitiesText: yup.string(),

        showSalaryB2B: yup.boolean(),

        showSalaryUZ: yup.boolean(),

        showSalaryUoP: yup.boolean(),

        specialization: yup.string()
            .required('Specialization is required'),

    });


    const {
        register: registerCreateOffer,
        handleSubmit: handleCreateOffer,
        control: controlCreateOffer,
        formState: { errors: createOfferErrors },
        watch: watchForm
    } = useForm({
        resolver: yupResolver(createOfferSchema)
    })

    const formValues = watchForm();

    const [requiredTechnologies, setRequiredTechnologies] = React.useState<Array<Technology>>([])
    const [niceToHaveTechnologies, setNiceToHaveTechnologies] = React.useState<Array<Technology>>([])
    const [responsibilities, setResponsibilities] = React.useState<Array<string>>([])
    const [requirements, setRequirements] = React.useState<Array<string>>([])
    const [niceToHave, setNiceToHave] = React.useState<Array<string>>([])
    const [whatWeOffer, setWhatWeOffer] = React.useState<Array<string>>([])
    const [questionsList, setQuestionsList] = React.useState<Array<QuestionWithType>>([])



    const onCreateOfferSubmit = (data: any) => {
        const request = {...emptyCreateOfferRequest}
        request.name = data.name
        request.firmName = data.firmName
        request.jobLevel = data.jobLevel
        request.operatingMode = data.operatingMode
        request.specialization = data.specialization
        request.localization = data.localization
        request.address = data.address
        request.expirationDate = formatExpirationDate(data.expirationDate, data.expirationTime)
        request.aboutProject = data.aboutProject
        request.responsibilitiesText = data.responsibilitiesText
        request.responsibilities = responsibilities
        request.requirements = requirements
        request.niceToHave = niceToHave
        request.whatWeOffer = whatWeOffer
        request.requiredTechnologies = requiredTechnologies.map((element: Technology) => (element.id))
        request.niceToHaveTechnologies = niceToHaveTechnologies.map((element: Technology) => (element.id))
        request.questions = convertQuestionsListToListOfOpenQuestions(questionsList)
        request.radioQuestions = convertQuestionsListToListOfRadioQuestions(questionsList)
        request.multipleChoiceQuestions = convertQuestionsListToListOfMultipleChoiceQuestions(questionsList)

        if (cvFile) {
            if (filesStore.presignedUrlForCompanyImage) {
                request.imageUrl = filesStore.presignedUrlForCompanyImage.key
            }
        }

        if (data.isUoP === true) {
            if (data.showSalaryUoP === true) {
                if (data.monthlyOrHourlyUoP === "monthly") {
                    request.isSalaryMonthlyUoP = true
                }
                else {
                    request.isSalaryMonthlyUoP = false
                }
                request.minSalaryUoP = data.minSalaryUoP
                request.maxSalaryUoP = data.maxSalaryUoP
            }
            else {
                request.isSalaryMonthlyUoP = false
            }
        }

        if (data.isB2B === true) {
            if (data.showSalaryB2B === true) {
                if (data.monthlyOrHourlyB2B === "monthly") {
                    request.isSalaryMonthlyB2B = true
                }
                else {
                    request.isSalaryMonthlyB2B = false
                }
                request.minSalaryB2B = data.minSalaryB2B
                request.maxSalaryB2B = data.maxSalaryB2B
            }
            else {
                request.isSalaryMonthlyB2B = false
            }
        }

        if (data.isUZ === true) {
            if (data.showSalaryUZ === true) {
                if (data.monthlyOrHourlyUZ === "monthly") {
                    request.isSalaryMonthlyUZ = true
                }
                else {
                    request.isSalaryMonthlyUZ = false
                }
                request.minSalaryUZ = data.minSalaryUZ
                request.maxSalaryUZ = data.maxSalaryUZ
            }
            else {
                request.isSalaryMonthlyUZ = false
            }
        }

        setReq(request)
        if (cvFile) {
            if (filesStore.presignedUrlForCompanyImage) {
                dispatch(uploadFileWithPresignedUrl({ presignedUrl: filesStore.presignedUrlForCompanyImage.url, file: cvFile }))
                setCreateButtonDisabled(true)
            }
            else {
                toast({
                    variant: "destructive",
                    title: "An error occurred while uploading the file!",
                });
            }


        }
        else {
            dispatch(createOffer(request))
        }

    }


    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const fileList = e.target.files;
        if (fileList && fileList.length > 0) {
            setCvFile(fileList[0]);
        }
    };

    const clearFile = () => {
        setCvFile(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
    };


    useEffect(() => {
        if (filesStore.success === "uploadFileWithPresignedUrl") {
            dispatch(createOffer(req))
            dispatch(setSuccessNullFiles())
        }
        else if (filesStore.fail === "uploadFileWithPresignedUrl") {
            toast({
                variant: "destructive",
                title: "An error occurred while uploading the file!",
            });
            setCreateButtonDisabled(false)
            dispatch(setFailNullFiles())
        }
    }, [filesStore.success, filesStore.fail])





    return (
        <div className='flex flex-col'>
            <Navbar />
            {profileStore.profile && <div className='flex flex-col items-center'>

                <form onSubmit={handleCreateOffer(onCreateOfferSubmit)}>
                    <div className='border-[1px] w-min mt-5 rounded-lg p-8'>
                        <div className='flex flex-row items-start w-full mb-2'>
                            <Button type='button' className='flex flex-row gap-x-1' onClick={() => navigate("/recruiter/manager")}><IoMdArrowRoundBack />Offers manager</Button>
                        </div>
                        <div className='flex flex-row justify-center mb-3'>
                            <h1 className='text-2xl font-bold'>Create a new job offer</h1>
                        </div>
                        <Separator />
                        <div className='flex flex-row flex-wrap gap-x-8 mt-6 justify-between'>
                            <div className='flex flex-col space-y-6'>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="title">Title</Label>
                                    <Input type="text" id="title" placeholder="Enter the job offer title" {...registerCreateOffer("name")} onKeyDown={(event) => { if (event.key === "Enter") { event.preventDefault(); } }} />
                                    <p className="text-red-500 text-sm font-normal">{createOfferErrors.name?.message}</p>
                                </div>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="firmName">Company name</Label>
                                    <Input type="text" id="firmName" placeholder="Enter the company name" {...registerCreateOffer("firmName")} onKeyDown={(event) => { if (event.key === "Enter") { event.preventDefault(); } }} />
                                    <p className="text-red-500 text-sm font-normal">{createOfferErrors.firmName?.message}</p>
                                </div>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="localization">Company address: city</Label>
                                    <Input type="text" id="localization" placeholder="Warszawa" {...registerCreateOffer("localization")} onKeyDown={(event) => { if (event.key === "Enter") { event.preventDefault(); } }} />
                                    <p className="text-red-500 text-sm font-normal">{createOfferErrors.localization?.message}</p>
                                </div>
                                <div className="grid w-full max-w-sm items-center gap-2">
                                    <Label htmlFor="address">Company address: street name and building number</Label>
                                    <Input type="text" id="address" placeholder="Kolejowa 14/20" {...registerCreateOffer("address")} onKeyDown={(event) => { if (event.key === "Enter") { event.preventDefault(); } }} />
                                    <p className="text-red-500 text-sm font-normal">{createOfferErrors.address?.message}</p>
                                </div>



                            </div>


                            <div className='flex flex-col rounded-lg bg-my-card p-4 w-min border-[1px] space-y-6 h-min'>
                                <SelectJobLevel control={controlCreateOffer} error={createOfferErrors.jobLevel?.message} />
                                <SelectOperatingMode control={controlCreateOffer} error={createOfferErrors.operatingMode?.message} />
                                <SelectSpecialization control={controlCreateOffer} error={createOfferErrors.specialization?.message} />
                            </div>

                            <div className='flex flex-col'>
                                <div className='flex flex-col rounded-lg bg-my-card p-4 w-full h-min border-[1px] space-y-6'>
                                    <ExpirationDatePicker control={controlCreateOffer} error={createOfferErrors.expirationDate?.message}/>
                                    <div className='flex flex-col space-y-2'>
                                        <Label htmlFor="expirationTime" >Offer expiration time</Label>
                                        <input type='time' className='text-md bg-background p-1 pl-3 border-[1px] rounded-lg w-[6rem] text-sm' id="expirationTime" {...registerCreateOffer('expirationTime')}></input>
                                        {createOfferErrors.expirationTime?.message && <p className="text-red-500 text-sm font-normal">{createOfferErrors.expirationTime?.message}</p>}
                                    </div>
                                </div>

                                <div className='flex flex-col space-y-4 mt-8 mb-8 justify-center'>
                                    <SelectTechnologiesDialog technologies={requiredTechnologies} setTechnologies={setRequiredTechnologies} text="Change required technologies" />
                                    <SelectTechnologiesDialog technologies={niceToHaveTechnologies} setTechnologies={setNiceToHaveTechnologies} text="Change optional technologies" />

                                </div>

                                <div className='flex flex-col gap-y-2 items-center'>

                                    <div className="grid items-center gap-1.5">
                                        <Label htmlFor="cv" className='text-base'>Attach a picture with your company logo</Label>
                                        <Input id="cv" type="file" ref={fileInputRef} onChange={handleFileChange} />

                                    </div>
                                    <Button className='w-full' variant={'outline'} type='button' onClick={clearFile} disabled={!cvFile}>Delete picture</Button>
                                </div>


                            </div>


                        </div>




                        <div className='flex flex-col mt-8'>
                            <p className='font-bold'>Bullet points</p>
                            <div className='flex flex-col flex-wrap w-full mb-16 mt-3'>
                                <div className='flex flex-row w-full mb-4'>
                                    <div className='mr-4 w-full'>
                                        <EditBulletPoints list={responsibilities} setList={setResponsibilities} text={"Responsibilities"} placeholder={"What are the responsibilities?"} />
                                    </div>
                                    <div className='w-full'>
                                        <EditBulletPoints list={requirements} setList={setRequirements} text={"Requirements"} placeholder={"What are the requirements?"} />
                                    </div>
                                </div>
                                <div className='flex flex-row w-full'>
                                    <div className='mr-4 w-full'>
                                        <EditBulletPoints list={niceToHave} setList={setNiceToHave} text={"Nice to have"} placeholder={"What is optional?"} />
                                    </div>
                                    <div className='w-full'>
                                        <EditBulletPoints list={whatWeOffer} setList={setWhatWeOffer} text={"What we offer"} placeholder={"What do you offer?"} />
                                    </div>
                                </div>

                            </div>
                        </div>




                        <div className='flex flex-row w-full space-x-8 mt-0'>
                            <div className='flex flex-col space-y-6 w-full'>
                                <div className="grid w-full gap-2">
                                    <Label htmlFor="responsibilitiesText">Responsibilities</Label>
                                    <Textarea className='h-36' id="responsibilitiesText" {...registerCreateOffer('responsibilitiesText')} />
                                </div>
                                <div className="grid w-full gap-2">
                                    <Label htmlFor="aboutProject">About project</Label>
                                    <Textarea className='h-36' id="aboutProject" {...registerCreateOffer('aboutProject')} />
                                </div>
                            </div>

                        </div>





                        <div className='flex flex-col space-y-3 mt-16 mb-16'>
                            <p className='font-bold'>What types of contracts do you offer?</p>
                            <div className='flex flex-row space-x-4'>

                                <ContractType isContractCheckbox={{ registerAs: "isUoP", label: "Employment contract" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryUoP" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyUoP" }}
                                    inputMinSalary={{
                                        props: {
                                            ...registerCreateOffer("minSalaryUoP", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    inputMaxSalary={{
                                        props: {
                                            ...registerCreateOffer("maxSalaryUoP", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    control={controlCreateOffer}
                                    errorMonthlyOrHourly={createOfferErrors.monthlyOrHourlyUoP?.message}
                                    errorMin={createOfferErrors.minSalaryUoP?.message}
                                    errorMax={createOfferErrors.maxSalaryUoP?.message}
                                    disabled={!formValues.isUoP}
                                    disabledSalary={!formValues.showSalaryUoP}
                                />
                                <ContractType isContractCheckbox={{ registerAs: "isB2B", label: "B2B" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryB2B" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyB2B" }}
                                    inputMinSalary={{
                                        props: {
                                            ...registerCreateOffer("minSalaryB2B", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    inputMaxSalary={{
                                        props: {
                                            ...registerCreateOffer("maxSalaryB2B", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    control={controlCreateOffer}
                                    errorMonthlyOrHourly={createOfferErrors.monthlyOrHourlyB2B?.message}
                                    errorMin={createOfferErrors.minSalaryB2B?.message}
                                    errorMax={createOfferErrors.maxSalaryB2B?.message}
                                    disabled={!formValues.isB2B}
                                    disabledSalary={!formValues.showSalaryB2B}
                                />
                                <ContractType isContractCheckbox={{ registerAs: "isUZ", label: "Order contract" }}
                                    showSalaryCheckbox={{ registerAs: "showSalaryUZ" }}
                                    selectMonthlyOrHourly={{ registerAs: "monthlyOrHourlyUZ" }}
                                    inputMinSalary={{
                                        props: {
                                            ...registerCreateOffer("minSalaryUZ", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    inputMaxSalary={{
                                        props: {
                                            ...registerCreateOffer("maxSalaryUZ", {
                                                setValueAs: (value) => { if (value) { return Number(value) } else { return null } }
                                            })
                                        }
                                    }}
                                    control={controlCreateOffer}
                                    errorMonthlyOrHourly={createOfferErrors.monthlyOrHourlyUZ?.message}
                                    errorMin={createOfferErrors.minSalaryUZ?.message}
                                    errorMax={createOfferErrors.maxSalaryUZ?.message}
                                    disabled={!formValues.isUZ}
                                    disabledSalary={!formValues.showSalaryUZ}
                                />




                            </div>
                        </div>






                        <EditRecruitmentQuestions questionsList={questionsList} setQuestionsList={setQuestionsList} />

                        <div className='flex flex-row justify-end mt-10'>
                            <Button type='submit' className='w-32' disabled={createButtonDisabled || filesStore.isLoading}>Create offer</Button>
                        </div>

                    </div>

                </form>
            </div>}
        </div>
    )
}

export default CreateOffer
