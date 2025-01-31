import React, { useState } from 'react'
import { Separator } from '../ui/separator'
import { Input } from '../ui/input'
import { Button } from '../ui/button'

const EditBulletPoints = (props: any) => {
    const [inputValue, setInputValue] = useState("")
    const handleAddButonClick = () => {
        if(inputValue.length > 0){
            props.setList([...props.list, inputValue])
        setInputValue("")
        }
        
    }

    const removeItem = (index: number) => {
        props.setList(props.list.filter((_: string, i: number) => i !== index));
    }

    return (
        <div className='flex flex-col space-y-3 p-4 border-[1px] rounded-lg w-full text-md bg-my-card'>
            <p>{props.text}</p>
            <Separator />
            <div className='flex flex-row space-x-3'>
                <Input type="text" placeholder="Example text" value={inputValue} onChange={(e) => setInputValue(e.target.value)} />
                <Button onClick={() => handleAddButonClick()}>Add</Button>

            </div>
            <div>
                <ul className='list-disc pl-5 break-words space-y-2'>
                    {props.list.map((element:string, index: number) => (
                        <li key={index}>
                            <div className='flex flex-row space-x-2 items-center justify-between'>
                                <span className='text-sm'>{element}</span>
                                <Button className='pt-2 h-[1.5rem] text-xs' variant={"secondary"} onClick={() => removeItem(index)}>Remove</Button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>

        </div>
    )
}

export default EditBulletPoints
