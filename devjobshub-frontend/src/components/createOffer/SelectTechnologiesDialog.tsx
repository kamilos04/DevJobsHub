import React, { useEffect, useState } from 'react'
import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useDispatch, useSelector } from 'react-redux'
import { searchTechnologies } from '@/state/technology/action'
import { Technology } from '@/types/technology'
import TechnologyBadge from '../technology/TechnologyBadge'
import { DialogClose } from '@radix-ui/react-dialog'
import CreateTechnologyDialog from '../technology/CreateTechnologyDialog'


const SelectTechnologiesDialog = ({ technologies, setTechnologies, text }: { technologies: Array<Technology>, setTechnologies: any, text: string }) => {
    const technologyStore = useSelector((store: any) => store.technology)
    const dispatch = useDispatch<any>()
    const [searchText, setSearchText] = useState("")
    const [openCreateTechnologyDialog, setOpenCreateTechnologyDialog] = useState(false)

    useEffect(() => {
        dispatch(searchTechnologies(searchText))
    }, [searchText])
    return (
        <div>
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant="outline">{text}</Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[27rem] space-y-0">
                    <DialogHeader>
                        <DialogTitle>{text}</DialogTitle>

                    </DialogHeader>
                    <div className='flex flex-col'>
                        <div className='flex flex-col space-y-3'>
                            <div className='flex flex-col'>
                                <Input
                                    className="col-span-3"
                                    placeholder='Search'
                                    onChange={e => setSearchText(e.target.value)}
                                    value={searchText}
                                />
                            </div>
                            <div className='flex flex-row items-start flex-wrap'>
                                {technologyStore.technologies?.content?.map((element: Technology) => {
                                    const exists = technologies.some(tech => tech.id === element.id);
                                    if (!exists) {
                                        return (<Button className='flex-1 mb-2 mr-2' variant="outline" key={element.id} onClick={() => setTechnologies([...technologies, element])}>{element.name}</Button>)
                                    }
                                })}
                            </div>
                        </div>
                        <span className='text-sm mt-1 cursor-pointer' onClick={() => setOpenCreateTechnologyDialog(true)} >Can't find the technology? Create a new one.</span>
                        <CreateTechnologyDialog open={openCreateTechnologyDialog} setOpen={setOpenCreateTechnologyDialog}/>
                        


                        <div className='flex flex-col'>
                            <p className='mt-7 mb-1'>Selected: </p>


                            <div className='flex flex-row items-star flex-wrap'>

                                {technologies.map((element: Technology) => <TechnologyBadge technology={element} key={element.id} technologies={technologies} setTechnologies={setTechnologies} />)}
                            </div>
                            <p className='text-sm font-light'>Click on the badge to remove it</p>
                        </div>
                    </div>


                    <DialogFooter>
                        <DialogClose asChild>
                            <Button variant="secondary">Save changes</Button>
                        </DialogClose>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    )
}

export default SelectTechnologiesDialog
