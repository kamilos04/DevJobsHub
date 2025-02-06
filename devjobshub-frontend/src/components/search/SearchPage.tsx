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

const SearchPage = () => {
  // const [request, setRequest] = React.useState<any>()
  const [technologies, setTechnologies] = React.useState<Array<Technology>>([])
  const dispatch = useDispatch<any>()
  const storeOffer = useSelector((store: any) => store.offer)

  const handleSubmitSearch = (data: any) => {
    const specializationsString = specializations.filter(element => data[element]).join(",")
    const operatingModesString = operatingModes.filter(element => data[element]).join(",")
    const jobLevelsString = jobLevels.filter(element => data[element]).join(",")
    const text = data.text

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
    params += "numberOfElements=10&"
    params += "pageNumber=0&"
    params += "sortBy=dateTimeOfCreation&"
    params += "sortingDirection=ASC&"
    console.log(data)
    dispatch(searchOffers(params))
  }



  const {
    register: registerSearch,
    handleSubmit: handleSearch,
    control: controlSearch,
    formState: { errors: searchErrors },
    watch: watchSearch
  } = useForm({
    // resolver: yupResolver(createOfferSchema)
  })

  // const data = watchSearch()

  const specializations = ["BACKEND", "FRONTEND", "FULLSTACK", "MOBILE", "EMBEDDED", "ARCHITECTURE", "TESTING", "SECURITY", "AIML", "DATASCIENCE", "HELPDESK", "UXUI", "ADMINISTRATOR", "PROJECTMANAGER", "OTHER"]
  const operatingModes = ["REMOTE", "STATIONARY", "HYBRID"]
  const jobLevels = ["TRAINEE", "JUNIOR", "MID", "SENIOR", "MANAGER", "CLEVEL"]


  return (
    <div className='flex flex-col'>
      <Navbar />
      <div className='flex flex-col items-center'>
        <form onSubmit={handleSearch(handleSubmitSearch)}>
          <div className='bg-black mt-8 p-4 w-[90rem] flex flex-col items-center'>
            <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-[60rem]'>
              <div className='flex flex-row justify-start mb-2'>
                <h1 className='text-2xl mb-1'>What kind of job are you looking for?</h1>
              </div>

              <div className='flex flex-row'>
                <Input type="text" placeholder="Search" className='h-12 rounded-none' {...registerSearch('text')} />

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
                <Button type='submit' className='h-12 ml-6 rounded-2xl'><div className='flex flex-row items-center space-x-2'><FaSearch /> <span className='text-sm'>Search</span></div></Button>
              </div>

              {/* {storeOffer.searchOffers?.content?.map((element: any) => <span>{element.id}</span>)} */}
            </div>
            <div className='flex flex-row w-full mt-8'>
              <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px] w-80'>
                <PopoverTechnologies technologies={technologies} setTechnologies={setTechnologies}/>
                <Label htmlFor="firmName" className='mt-3'>Technologies: </Label>
                <div className='flex flex-row items-start flex-wrap mt-2 w-full gap-x-2 gap-y-2'>
                  {technologies.map((element: Technology) => <Badge className='cursor-pointer text-sm bg-gray-400' onClick={() => setTechnologies(technologies.filter((item: Technology) => item.id !== element.id))}>{element.name}</Badge>)}
                  {technologies.length===0 && <span className='text-sm text-gray-400'>No technology selected</span>}
                </div>
                
              </div>
              <div>
                <div className='flex flex-row space-x-4'>
                  <Controller
                    name="sortBy"
                    control={controlSearch}
                    defaultValue={"dateTimeOfCreation"}

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
                    defaultValue={"ASC"}

                    render={({ field }) => (
                      <>
                        <Select value={field.value} onValueChange={field.onChange}>
                          <SelectTrigger className="w-auto">
                            Sorting direction: {field.value === "ASC" ? "Ascending" : "Descending"}
                          </SelectTrigger>
                          <SelectContent>
                            <SelectGroup>
                              <SelectItem value="ASC">Ascending</SelectItem>
                              <SelectItem value="DSC">Descending</SelectItem>
                            </SelectGroup>
                          </SelectContent>
                        </Select>
                      </>
                    )}
                  />



                </div>



              </div>

            </div>

          </div>
        </form>
      </div>

    </div>
  )
}

export default SearchPage
