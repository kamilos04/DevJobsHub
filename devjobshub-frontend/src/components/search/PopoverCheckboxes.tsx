import React from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import { Checkbox } from "@/components/ui/checkbox"
import { Controller } from 'react-hook-form'

const PopoverCheckboxes = ({ text, control, checkboxes={} }: any) => {
    return (
        <div>
            <Popover>
                <PopoverTrigger asChild>
                    <Button variant="outline" className='h-12 rounded-none w-40'>{text}</Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto">
                    <div className="p-1">
                        <div className="flex flex-col space-y-4">
                            {checkboxes?.map((element: any, index: number) => <div className="flex items-center space-x-2" key={index}>
                                <Controller
                                    name={element.registerAs}
                                    control={control}
                                    defaultValue={false}
                                    
                                    render={({ field }) => (
                                        <>
                                            <Checkbox {...field} onCheckedChange={field.onChange} checked={field.value} id={element.registerAs}/>
                                            <label
                                                htmlFor={element.registerAs}
                                                className="text-sm leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 font-bold"
                                            >
                                                {element.label}
                                            </label>
                                        </>
                                    )}
                                />

                            </div>)}
                            

                        </div>
                    </div>
                </PopoverContent>
            </Popover>
        </div>
    )
}

export default PopoverCheckboxes
