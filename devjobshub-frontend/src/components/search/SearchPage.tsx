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
import { useForm } from 'react-hook-form'

const SearchPage = () => {
  const [request, setRequest] = React.useState<any>()
  const handleSubmitSearch = () => {

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

  const searchValues=watchSearch()

  useEffect(() => {
    console.log(searchValues)
  }, [searchValues])


  return (
    <div className='flex flex-col'>
      <Navbar />
      <div className='flex flex-col items-center'>
        <div className='bg-black mt-8 p-4 w-[80rem]'>
          <div className='bg-my-card flex flex-col p-4 rounded-xl border-[1px]'>
            <div className='flex flex-row justify-start mb-2'>
              <h1 className='text-2xl mb-1'>What kind of job are you looking for?</h1>
            </div>
            <form>
              <div className='flex flex-row'>
                <Input type="text" placeholder="Search" className='h-12 rounded-none' {...registerSearch('text')}/>

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
              </div>
            </form>
          </div>
        </div>
      </div>

    </div>
  )
}

export default SearchPage
