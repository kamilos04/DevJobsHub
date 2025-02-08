import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { Input } from "@/components/ui/input"
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import PopoverCheckboxes from './PopoverCheckboxes'
import { Controller, useForm } from 'react-hook-form'
import { Button } from '../ui/button'
import { FaSearch } from "react-icons/fa";
import { useDispatch, useSelector, useStore } from 'react-redux'
import { searchOffers } from '@/state/offer/action'
import { Technology } from '@/types/technology'
import PopoverTechnologies from './PopoverTechnologies'
import { SelectItemText } from '@radix-ui/react-select'
import { Badge } from "@/components/ui/badge"
import { Label } from '../ui/label'
import { Separator } from "@/components/ui/separator"
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"
import OfferCard from './OfferCard'
import { Offer } from '@/types/offer'
import { useNavigate, useSearchParams } from 'react-router'
import { getTechnologiesByIds } from '@/state/technology/action'
import { text } from 'stream/consumers'


const SearchPage = () => {

  // const [request, setRequest] = React.useState<any>()
  const [technologies, setTechnologies] = React.useState<Array<Technology>>([])
  const dispatch = useDispatch<any>()
  const storeOffer = useSelector((store: any) => store.offer)
  const storeTechnology = useSelector((store: any) => store.technology)
  const [localizationText, setLocalizationText] = React.useState<string>("")
  const [localizations, setLocalizations] = React.useState<Array<string>>([])
  const [pageNumber, setPageNumber] = React.useState<number>(0)
  const navigate = useNavigate()
  const [searchParams, setSearchParams] = useSearchParams();
  
  
  
  const {
    register: registerSearch,
    handleSubmit: handleSubmit,
    control: controlSearch,
    formState: { errors: searchErrors },
    setValue: setValueSearch,
    watch: watchSearch
  } = useForm({
    // resolver: yupResolver(createOfferSchema)
    // defaultValues: {sortBy: "dateTimeOfCreation", sortingDirection: "asc"}

  })

  // const formValues = watchSearch();

  const handleSubmitSearch = () => {
    const searchValues = watchSearch();
    // console.log(formValues.sortBy)
    const specializationsString = specializations.filter(element => searchValues[element]).join(",")
    const operatingModesString = operatingModes.filter(element => searchValues[element]).join(",")
    const jobLevelsString = jobLevels.filter(element => searchValues[element]).join(",")
    const text = searchValues.text

    let params = ""
    if (specializationsString) {
      params += `specializations=${specializationsString}&`
    }
    if (operatingModesString) {
      params += `operatingModes=${operatingModesString}&`
    }
    if (jobLevelsString) {
      params += `jobLevels=${jobLevelsString}&`
    }
    if (text) {
      params += `text=${text}&`
    }
    if (technologies.length !== 0) {
      params += `technologies=${technologies.map(t => t.id).join(",")}&`
    }
    if (localizations.length !== 0) {
      params += `localizations=${localizations.map(l => l).join(",")}&`
    }
    params += "numberOfElements=10&"
    params += `pageNumber=${pageNumber}&`
    params += `sortBy=${searchValues.sortBy}&`
    params += `sortingDirection=${searchValues.sortingDirection}&`
    console.log(params)
    dispatch(searchOffers(params))
  }


  const handleCreateNewUrl = (data: any) => {
    // console.log(data)
    const paramsURL = new URLSearchParams()

    const specializationsString = specializations.filter(element => data[element]).join(",")
    const operatingModesString = operatingModes.filter(element => data[element]).join(",")
    const jobLevelsString = jobLevels.filter(element => data[element]).join(",")
    const text = data.text

    if (specializationsString) {
      paramsURL.set("specializations", specializationsString)
    }
    if (operatingModesString) {
      paramsURL.set("operatingModes", operatingModesString)
    }
    if (jobLevelsString) {
      paramsURL.set("jobLevels", jobLevelsString)
    }
    if (text) {
      paramsURL.set("text", text)
    }
    if (technologies.length !== 0) {
      const technologiesIdsString = technologies.map(t => t.id).join(",")
      paramsURL.set("technologies", technologiesIdsString)
    }
    if (localizations.length !== 0) {
      const localizationsString = localizations.map(l => l).join(",")
      paramsURL.set("localizations", localizationsString)
    }
    paramsURL.set("pageNumber", pageNumber.toString())
    paramsURL.set("sortBy", data.sortBy)
    paramsURL.set("sortingDirection", data.sortingDirection)
    setSearchParams(paramsURL)
  }


  



  // const defaultValues = {
  //   text: searchParams.get("text") || "",xxxx
  //   specializations: searchParams.get("specializations")?.split(",") || [],xxx
  //   operatingModes: searchParams.get("operatingModes")?.split(",") || [],xxx
  //   jobLevels: searchParams.get("jobLevels")?.split(",") || [],xxx
  //   sortBy: searchParams.get("sortBy") || "dateTimeOfCreation",xxxx
  //   sortingDirection: searchParams.get("sortingDirection") || "asc",xxx
  //   pageNumber: Number(searchParams.get("pageNumber")) || 0,xxxx
  //   localizations: searchParams.get("localizations") || []xxxx
  // };

  // const data = watchSearch()
  
  const specializations = ["BACKEND", "FRONTEND", "FULLSTACK", "MOBILE", "EMBEDDED", "ARCHITECTURE", "TESTING", "SECURITY", "AIML", "DATASCIENCE", "HELPDESK", "UXUI", "ADMINISTRATOR", "PROJECTMANAGER", "OTHER"]
  const operatingModes = ["REMOTE", "STATIONARY", "HYBRID"]
  const jobLevels = ["TRAINEE", "JUNIOR", "MID", "SENIOR", "MANAGER", "CLEVEL"]

  // useEffect(() =>{
  //   console.log(formValues.sortBy)
  //   console.log("zmieniono")
  // }, [formValues.sortBy])


  // useEffect(() => {
  //   handleSearch(handleSubmitSearch)()
  // }, [pageNumber, formValues.sortingDirection, formValues.sortBy])

  useEffect(() => {
    if(searchParams.get("sortBy")===null || searchParams.get("sortingDirection")===null || searchParams.get("pageNumber")===null){
      console.log('PUSE')
      handleSubmit(handleCreateNewUrl)()
    }
    console.log("ustawiam")

    if(searchParams.get("text")){
      setValueSearch("text", searchParams.get("text"))
    }
    else{
      setValueSearch("text", "")
    }

    if(searchParams.get("sortBy")){
      setValueSearch("sortBy", searchParams.get("sortBy"))
    }
    else{
      setValueSearch("sortBy", "dateTimeOfCreation")
    }

    if(searchParams.get("sortingDirection")){
      setValueSearch("sortingDirection", searchParams.get("sortingDirection"))
    }
    else{
      setValueSearch("sortingDirection", "asc")
    }

    if(searchParams.get("pageNumber")){
      setPageNumber(Number(searchParams.get("pageNumber")))
    }
    else{
      setPageNumber(0)
    }

    if(searchParams.get("localizations")){
      setLocalizations(searchParams.get("localizations")?.split(",") || [])
    }
    else{
      setLocalizations([])
    }

    const specializationsList = searchParams.get("specializations")?.split(",") || []
    specializations.forEach((element) => {
      if(specializationsList.includes(element)){
        setValueSearch(element, true)
      }
      else{
        setValueSearch(element, false)
      }
    })

    const operatingModesList = searchParams.get("operatingModes")?.split(",") || []
    operatingModes.forEach((element) => {
      if(operatingModesList.includes(element)){
        setValueSearch(element, true)
      }
      else{
        setValueSearch(element, false)
      }
    })

    const jobLevelsList = searchParams.get("jobLevels")?.split(",") || []
    jobLevels.forEach((element) => {
      if(jobLevelsList.includes(element)){
        setValueSearch(element, true)
      }
      else{
        setValueSearch(element, false)
      }
    })

    if(searchParams.get("technologies")){
      const technologiesIdsString = searchParams.get("technologies") || ""

      dispatch(getTechnologiesByIds(technologiesIdsString))
    }
    else{
      setTechnologies([])
      handleSubmitSearch()
    }
    

    
  }, [searchParams])



  useEffect(() => {
    if(storeTechnology.success==="getTechnologiesByIds"){
      setTechnologies(storeTechnology.technologiesByIds)
      handleSubmitSearch()
    }
  }, [storeTechnology.success])


  // useEffect(() => {
  //   handleSubmit(handleCreateNewUrl)()
  // }, [pageNumber])

  return (
    <div className='flex flex-col'>
      <Navbar />
      <div className='flex flex-col items-center'>
        {/* <form onSubmit={handleSearch(handleSubmitSearch)}> */}
          <div className='mt-8 p-4 w-[90rem] flex flex-col items-center rounded-2xl'>
            <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-full'>
              <div className='flex flex-row justify-start mb-2'>
                <h1 className='text-2xl mb-1'>What kind of job are you looking for?</h1>
              </div>

              <div className='flex flex-row'>
                <Input type="text" placeholder="Search" className='h-12 rounded-none' {...registerSearch('text')} onKeyDown={(event) => {if (event.key === "Enter") {handleSubmit(handleCreateNewUrl)()}}}/>

                <PopoverCheckboxes text="Specializations" control={controlSearch} checkboxes={[
                  { registerAs: "BACKEND", label: "Backend" },
                  { registerAs: "FRONTEND", label: "Frontend" },
                  { registerAs: "FULLSTACK", label: "Fullstack" },
                  { registerAs: "MOBILE", label: "Mobile" },
                  { registerAs: "EMBEDDED", label: "Embedded" },
                  { registerAs: "ARCHITECTURE", label: "Architecture" },
                  { registerAs: "TESTING", label: "Testing" },
                  { registerAs: "SECURITY", label: "Security" },
                  { registerAs: "AIML", label: "AI/ML" },
                  { registerAs: "DATASCIENCE", label: "Data Science" },
                  { registerAs: "HELPDESK", label: "Helpdesk" },
                  { registerAs: "UXUI", label: "UX/UI" },
                  { registerAs: "ADMINISTRATOR", label: "Administrator" },
                  { registerAs: "PROJECTMANAGER", label: "Project Manager" },
                  { registerAs: "OTHER", label: "Other" }
                ]} />
                <PopoverCheckboxes text="Operating modes" control={controlSearch} checkboxes={[{ registerAs: "STATIONARY", label: "Stationary" }, { registerAs: "REMOTE", label: "Remote" }, { registerAs: "HYBRID", label: "Hybrid" }]} />
                <PopoverCheckboxes text="Job levels" control={controlSearch} checkboxes={[
                  { registerAs: "TRAINEE", label: "Trainee / Intern" },
                  { registerAs: "JUNIOR", label: "Junior" },
                  { registerAs: "MID", label: "Mid" },
                  { registerAs: "SENIOR", label: "Senior" },
                  { registerAs: "MANAGER", label: "Manager" },
                  { registerAs: "CLEVEL", label: "C-level" },
                ]} />
                <Button type='submit' className='h-12 ml-6 rounded-2xl'><div className='flex flex-row items-center space-x-2' onClick={handleSubmit(handleCreateNewUrl)}><FaSearch /> <span className='text-sm'>Search</span></div></Button>
              </div>

              {/* {storeOffer.searchOffers?.content?.map((element: any) => <span>{element.id}</span>)} */}
            </div>
            <div className='flex flex-row w-full mt-8'>
              <div className='bg-my-card flex flex-col rounded-xl border-[1px] w-80 p-5 h-min'>
                <Label className=''>Technologies: </Label>
                <div className='flex flex-row items-start flex-wrap mt-2 w-full gap-x-2 gap-y-2 mb-2'>
                  {technologies.map((element: Technology) => <Badge key={element.id} className='cursor-pointer text-sm bg-gray-400' onClick={() => setTechnologies(technologies.filter((item: Technology) => item.id !== element.id))}>{element.name}</Badge>)}
                  {technologies.length === 0 && <span className='text-sm text-gray-400'>No technology selected</span>}
                </div>
                <PopoverTechnologies technologies={technologies} setTechnologies={setTechnologies} />


                <Separator className='mt-4 mb-4' />
                <Label className=''>Locations: </Label>
                <div className='flex flex-row items-start flex-wrap mt-2 w-full gap-x-2 gap-y-2 mb-2'>
                  {localizations.map((element: string, index: number) => <Badge key={element} className='cursor-pointer text-sm bg-gray-400' onClick={() => setLocalizations(localizations.filter((item: string, i: number) => i !== index))}>{element}</Badge>)}
                  {localizations.length === 0 && <span className='text-sm text-gray-400'>No locations selected</span>}
                </div>

                <div className='flex flex-row gap-x-2'>
                  <Input type="text" placeholder="Enter location" value={localizationText} onChange={(e) => {
                    setLocalizationText(e.target.value)
                  }}
                    onKeyDown={(event) => {
                      if (event.key === "Enter") {
                        event.preventDefault();
                      }
                    }} />

                  <Button type='button' className='w-20' onClick={() => {
                    if (localizationText !== "") {
                      setLocalizations([...localizations, localizationText])
                      setLocalizationText("")
                    }
                  }}>Add</Button>

                </div>



                <Button className='mt-4' variant={'secondary'} onClick={handleSubmit(handleCreateNewUrl)}>Filter</Button>
              </div>
              <div className='ml-4 flex-col w-full'>
                <div className='flex flex-row space-x-4 items-center'>
                  <Controller
                    name="sortBy"
                    control={controlSearch}
                    // defaultValue={"dateTimeOfCreation"}

                    render={({ field }) => (
                      <>
                        <Select value={field.value} onValueChange={field.onChange}>
                          <SelectTrigger className="w-auto">
                            <span>Sort by: {field.value === "dateTimeOfCreation" && "Creation date"}{field.value === "expirationDate" && "Expiration date"}{field.value === "name" && "Offer title"}</span>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectGroup>
                              <SelectItem value="dateTimeOfCreation">Creation date</SelectItem>
                              <SelectItem value="expirationDate">Expiration date</SelectItem>
                              <SelectItem value="name">Offer title</SelectItem>
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </>
                    )}
                  />
                  <Controller
                    name="sortingDirection"
                    control={controlSearch}
                    // defaultValue={"asc"}

                    render={({ field }) => (
                      <>
                        <Select value={field.value} onValueChange={field.onChange}>
                          <SelectTrigger className="w-auto">
                            Sorting direction: {field.value === "asc" ? "Ascending" : "Descending"}
                          </SelectTrigger>
                          <SelectContent>
                            <SelectGroup>
                              <SelectItem value="asc">Ascending</SelectItem>
                              <SelectItem value="dsc">Descending</SelectItem>
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </>
                    )}
                  />
                  {storeOffer.searchOffers && <span>{storeOffer.searchOffers?.totalElements} offers found</span>}


                </div>
                <div className='flex flex-col gap-y-4 mt-4 w-full'>
                  {storeOffer.searchOffers?.content?.map((element: Offer) => <OfferCard offer={element} key={element.id} />)}
                </div>
                {storeOffer.searchOffers?.totalElements===0 && <span className='text-gray-300 text-2xl'>{"No job offers found :("}</span>}




              </div>

            </div>
            <Pagination className='mt-3'>
              <PaginationContent>

                <PaginationItem>
                  <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                    if (pageNumber - 1 >= 0) {
                      setPageNumber(pageNumber - 1)
                    }

                  }} />
                </PaginationItem>
                {[...Array(storeOffer.searchOffers?.totalPages || 0)].map((_, i) => {
                  if (i >= pageNumber - 3 && i <= pageNumber + 3) {
                    return (
                      <PaginationItem key={i} >
                        <PaginationLink className='cursor-pointer select-none'
                          onClick={() => {
                            setPageNumber(i)
                          }}

                          isActive={pageNumber === i ? true : false}
                        >{i + 1}</PaginationLink>
                      </PaginationItem>)
                  }
                }
                )}
                {/* <PaginationItem>
                  <PaginationEllipsis />
                </PaginationItem> */}
                <PaginationItem>
                  <PaginationNext className='cursor-pointer select-none' onClick={() => {
                    if (pageNumber + 1 < storeOffer.searchOffers?.totalPages) {
                      setPageNumber(pageNumber + 1)
                    }
                  }} />
                </PaginationItem>
              </PaginationContent>
            </Pagination>
          </div>
        {/* </form> */}
      </div>

    </div>
  )
}

export default SearchPage
