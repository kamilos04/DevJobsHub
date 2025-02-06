import React, { useEffect, useRef } from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import { useDispatch, useSelector } from 'react-redux'
import { Technology } from '@/types/technology'
import { searchTechnologies } from '@/state/technology/action'

const PopoverTechnologies = ({ technologies = {}, setTechnologies = {} }: any) => {
    const [open, setOpen] = React.useState<boolean>(false)
    const inputRef = useRef<HTMLInputElement | null>(null);
    const technologyStore = useSelector((store: any) => store.technology)
    const [text, setText] = React.useState<string>("")
    const dispatch = useDispatch<any>()
    useEffect(() => {
        dispatch(searchTechnologies(text))
    }, [text])

    return (
        <Popover open={open} onOpenChange={setOpen} modal={false}>
            <PopoverTrigger asChild >
                <Input ref={inputRef} type="text" placeholder="Search for technology" value={text} onChange={(e) => {
                    setText(e.target.value)
                }}
                onKeyDown={(event) => {
                    if (event.key === "Enter") {
                        event.preventDefault();
                    }
                }}/>
            </PopoverTrigger>
            <PopoverContent className="w-80" onFocus={() => inputRef.current?.focus()} onOpenAutoFocus={(event) => event.preventDefault()}>
                <div className='flex flex-row items-start flex-wrap gap-x-2 gap-y-2'>
                    {technologyStore.technologies?.content?.map((element: Technology) => {
                        const exists = technologies.some((tech: Technology) => tech.id === element.id);
                        if (!exists) {
                            return (<Button className='flex-1' variant="outline" key={element.id} onClick={() => setTechnologies([...technologies, element])}>{element.name}</Button>)
                        }
                    })}
                </div>
            </PopoverContent>
        </Popover>

    )
}

export default PopoverTechnologies
