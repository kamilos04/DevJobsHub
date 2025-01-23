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

const SelectTechnologiesDialog = ({ technologies, setTechnologies, text }: { technologies: Array<Technology>, setTechnologies: any, text: string }) => {
    const technologyStore = useSelector((store: any) => store.technology)
    const dispatch = useDispatch<any>()
    const [searchText, setSearchText] = useState("")

    useEffect(() => {
        dispatch(searchTechnologies(searchText))
    }, [searchText])
    return (
        <div>
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant="outline">Edit Profile</Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle>{text}</DialogTitle>
                        {/* <DialogDescription>
                            Make changes to your profile here. Click save when you're done.
                        </DialogDescription> */}
                    </DialogHeader>
                    {/* <div className="grid gap-4 py-4">
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="name" className="text-right">
                                Name
                            </Label>
                            <Input
                                id="name"
                                defaultValue="Pedro Duarte"
                                className="col-span-3"
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="username" className="text-right">
                                Username
                            </Label>
                            <Input
                                id="username"
                                defaultValue="@peduarte"
                                className="col-span-3"
                            />
                        </div>
                    </div> */}
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
                    
                    <div className='flex flex-col'>
                        <p className='mt-5'>Selected: </p>
                        

                    <div className='flex flex-row items-star flex-wrap'>

                        {technologies.map((element: Technology) => <TechnologyBadge technology={element} key={element.id} technologies={technologies} setTechnologies={setTechnologies}/>)}
                    </div>
                    <p className='text-sm font-light'>Click on the badge to remove it</p>
                    </div>
                    
                    <DialogFooter>
                        <Button type="submit">Save changes</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    )
}

export default SelectTechnologiesDialog
