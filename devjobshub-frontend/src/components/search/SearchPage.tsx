import React, { useEffect } from 'react'
import Navbar from '../navbar/Navbar'
import { Input } from "@/components/ui/input"
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
} from "@/components/ui/select"
import PopoverCheckboxes from './PopoverCheckboxes'
import { useForm } from 'react-hook-form'
import { Button } from '../ui/button'
import { FaSearch } from "react-icons/fa";
import { useDispatch, useSelector } from 'react-redux'
import { searchOffers } from '@/state/offer/action'
import { Technology } from '@/types/technology'
import PopoverTechnologies from './PopoverTechnologies'
import { Badge } from "@/components/ui/badge"
import { Label } from '../ui/label'
import { Separator } from "@/components/ui/separator"
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"
import OfferCard from './OfferCard'
import { Offer } from '@/types/offer'
import { useSearchParams } from 'react-router'
import { getTechnologiesByIds } from '@/state/technology/action'
import { jobLevels, operatingModes, specializations } from '@/constants'


const SearchPage = () => {

  const [technologies, setTechnologies] = React.useState<Array<Technology>>([])
  const technologiesRef = React.useRef<Technology[]>([])
  const dispatch = useDispatch<any>()
  const storeOffer = useSelector((store: any) => store.offer)
  const storeTechnology = useSelector((store: any) => store.technology)
  const [localizationText, setLocalizationText] = React.useState<string>("")
  const [localizations, setLocalizations] = React.useState<Array<string>>([])
  const localizationsRef = React.useRef<string[]>([])
  const [pageNumber, setPageNumber] = React.useState<number>(0)
  const pageRef = React.useRef<number>(0)
  const [searchParams, setSearchParams] = useSearchParams();
  const [sortBy, setSortBy] = React.useState<string>("dateTimeOfCreation")
  const sortByRef = React.useRef<string>("dateTimeOfCreation")
  const [sortDirecrtion, setSortDirection] = React.useState<string>("asc")
  const sortDirecrtionRef = React.useRef<string>("asc")



  const {
    register: registerSearch,
    handleSubmit: handleSubmit,
    control: controlSearch,
    setValue: setValueSearch,
    watch: watchSearch
  } = useForm({
    // resolver: yupResolver(createOfferSchema)
    // defaultValues: {sortBy: "dateTimeOfCreation", sortingDirection: "asc"}

  })

  // const formValues = watchSearch();

  const handleSubmitSearch = () => {
    const searchValues = watchSearch();
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
    if (technologiesRef.current.length !== 0) {
      params += `technologies=${technologiesRef.current.map(t => t.id).join(",")}&`
    }
    if (localizationsRef.current.length !== 0) {
      params += `localizations=${localizationsRef.current.map(l => l).join(",")}&`
    }
    params += "numberOfElements=10&"
    params += `pageNumber=${pageRef.current}&`
    params += `sortBy=${sortByRef.current}&`
    params += `sortingDirection=${sortDirecrtionRef.current}&`
    dispatch(searchOffers(params))
  }


  const handleCreateNewUrl = (data: any) => {
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
    // paramsURL.set("pageNumber", pageNumber.toString()) 
    paramsURL.set("pageNumber", "0")
    paramsURL.set("sortBy", sortBy || "dateTimeOfCreation")
    paramsURL.set("sortingDirection", sortDirecrtion || "asc")
    setSearchParams(paramsURL)
  }





  useEffect(() => {
    if (searchParams.get("sortBy") === null || searchParams.get("sortingDirection") === null || searchParams.get("pageNumber") === null) {
      handleSubmit(handleCreateNewUrl)()
      return
    }

    if (searchParams.get("text")) {
      setValueSearch("text", searchParams.get("text"))
    }
    else {
      setValueSearch("text", "")
    }



    if (searchParams.get("sortBy")) {
      setSortBy(searchParams.get("sortBy") || "asc")
      sortByRef.current = searchParams.get("sortBy") || "asc"
    }

    if (searchParams.get("sortingDirection")) {
      setSortDirection(searchParams.get("sortingDirection") || "dateTimeOfCreation")
      sortDirecrtionRef.current = searchParams.get("sortingDirection") || "dateTimeOfCreation"
    }


    if (searchParams.get("pageNumber")) {
      setPageNumber(Number(searchParams.get("pageNumber")))
      pageRef.current = Number(searchParams.get("pageNumber"))
    }


    if (searchParams.get("localizations")) {
      setLocalizations(searchParams.get("localizations")?.split(",") || [])
      localizationsRef.current = searchParams.get("localizations")?.split(",") || []
    }
    else {
      setLocalizations([])
      localizationsRef.current = []
    }

    const specializationsList = searchParams.get("specializations")?.split(",") || []
    specializations.forEach((element) => {
      if (specializationsList.includes(element)) {
        setValueSearch(element, true)
      }
      else {
        setValueSearch(element, false)
      }
    })

    const operatingModesList = searchParams.get("operatingModes")?.split(",") || []
    operatingModes.forEach((element) => {
      if (operatingModesList.includes(element)) {
        setValueSearch(element, true)
      }
      else {
        setValueSearch(element, false)
      }
    })

    const jobLevelsList = searchParams.get("jobLevels")?.split(",") || []
    jobLevels.forEach((element) => {
      if (jobLevelsList.includes(element)) {
        setValueSearch(element, true)
      }
      else {
        setValueSearch(element, false)
      }
    })

    if (searchParams.get("technologies")) {
      const technologiesIdsString = searchParams.get("technologies") || ""

      dispatch(getTechnologiesByIds(technologiesIdsString))
    }
    else {
      setTechnologies([])
      technologiesRef.current = []
      handleSubmitSearch()
    }



  }, [searchParams])



  useEffect(() => {
    if (storeTechnology.success === "getTechnologiesByIds") {
      setTechnologies(storeTechnology.technologiesByIds)
      technologiesRef.current = storeTechnology.technologiesByIds
      handleSubmitSearch()
    }
  }, [storeTechnology.success])




const setPageStateAndSearchParams = (value: number) => {
  setPageNumber(value)
  setSearchParams((prevParams) => {
    const newParams = new URLSearchParams(prevParams);
    newParams.set("pageNumber", String(value));
    return newParams;
  });
}

  return (
    <div className='flex flex-col'>
      <Navbar />
      <div className='flex flex-col items-center'>
        {/* <form onSubmit={handleSearch(handleSubmitSearch)}> */}
        <div className='mt-8 p-4 w-[90rem] flex flex-col items-center rounded-2xl'>
          <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-full'>
            <div className='flex flex-row justify-start mb-2'>
              <h1 className='text-2xl mb-1 font-bold'>What kind of job are you looking for?</h1>
            </div>

            <div className='flex flex-row'>
              <Input type="text" placeholder="Search" className='h-12 rounded-none' {...registerSearch('text')} onKeyDown={(event) => { if (event.key === "Enter") { handleSubmit(handleCreateNewUrl)() } }} />

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
              <Button type='submit' className='h-12 ml-6 rounded-2xl' onClick={handleSubmit(handleCreateNewUrl)}><div className='flex flex-row items-center space-x-2' ><FaSearch /> <span className='text-sm'>Search</span></div></Button>
            </div>

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
                {localizations.map((element: string, index: number) => <Badge key={element} className='cursor-pointer text-sm bg-gray-400' onClick={() => setLocalizations(localizations.filter((_item: string, i: number) => i !== index))}>{element}</Badge>)}
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
                <Select value={sortBy} onValueChange={(value: string) => {
                  setSortBy(value)
                  setSearchParams((prevParams) => {
                    const newParams = new URLSearchParams(prevParams);
                    newParams.set("sortBy", value)
                    return newParams;
                  });
                }}>
                  <SelectTrigger className="w-auto">
                    <span>Sort by: {sortBy === "dateTimeOfCreation" && "Creation date"}{sortBy === "expirationDate" && "Expiration date"}{sortBy === "name" && "Offer title"}</span>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="dateTimeOfCreation">Creation date</SelectItem>
                      <SelectItem value="expirationDate">Expiration date</SelectItem>
                      <SelectItem value="name">Offer title</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>

                <Select value={sortDirecrtion} onValueChange={(value: string) => {
                  setSortDirection(value)
                  setSearchParams((prevParams) => {
                    const newParams = new URLSearchParams(prevParams);
                    newParams.set("sortingDirection", value)
                    return newParams;
                  });
                }}>
                  <SelectTrigger className="w-auto">
                    Sorting direction: {sortDirecrtion === "asc" ? "Ascending" : "Descending"}
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="asc">Ascending</SelectItem>
                      <SelectItem value="dsc">Descending</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
                {storeOffer.searchOffers && <span>{storeOffer.searchOffers?.totalElements} offers found</span>}


              </div>
              <div className='flex flex-col gap-y-4 mt-4 w-full'>
                {storeOffer.searchOffers?.content?.map((element: Offer) => <OfferCard offer={element} key={element.id} />)}
              </div>
              {storeOffer.searchOffers?.totalElements === 0 && <span className='text-gray-300 text-2xl'>{"No job offers found :("}</span>}




            </div>

          </div>
          {(storeOffer.searchOffers?.totalElements > 0) && <Pagination className='mt-3'>
            <PaginationContent>

              <PaginationItem>
                <PaginationPrevious className='cursor-pointer select-none' onClick={() => {
                  if (pageNumber - 1 >= 0) {

                    setPageStateAndSearchParams(pageNumber - 1)
                  }

                }} />
              </PaginationItem>
              {[...Array(storeOffer.searchOffers?.totalPages || 0)].map((_, i) => {
                if (i >= pageNumber - 3 && i <= pageNumber + 3) {
                  return (
                    <PaginationItem key={i} >
                      <PaginationLink className='cursor-pointer select-none'
                        onClick={() => {

                          setPageStateAndSearchParams(i)
                        }}

                        isActive={pageNumber === i ? true : false}
                      >{i + 1}</PaginationLink>
                    </PaginationItem>)
                }
              }
              )}
              <PaginationItem>
                <PaginationNext className='cursor-pointer select-none' onClick={() => {
                  if (pageNumber + 1 < storeOffer.searchOffers?.totalPages) {
                    setPageStateAndSearchParams(pageNumber + 1)
                  }
                }} />
              </PaginationItem>
            </PaginationContent>
          </Pagination>}
        </div>
        {/* </form> */}
      </div>

    </div>
  )
}

export default SearchPage
