import React from 'react'
import { Controller } from 'react-hook-form'
import { Label } from '../ui/label'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select'
import { Popover, PopoverContent, PopoverTrigger } from '../ui/popover'
import { Button } from '../ui/button'
import { CalendarIcon } from 'lucide-react'
import { Calendar } from '../ui/calendar'
import { format } from "date-fns"
import { cn } from "@/lib/utils"

const ExpirationDatePicker = ({ control }: any) => {
    return (
        <div>
            <Controller
                name="expirationDate"
                control={control}
                render={({ field }) => (
                    <>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="expirationDate">Offer expiration date</Label>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant={"outline"}
                                        className={cn(
                                            "w-[280px] justify-start text-left font-normal",
                                            !field.value && "text-muted-foreground"
                                        )}
                                    >
                                        <CalendarIcon />
                                        {field.value ? format(field.value, "PPP") : <span>Pick a date</span>}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        mode="single"
                                        selected={field.value}
                                        onSelect={field.onChange}
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>

                        </div>
                    </>
                )}
            />
        </div>
    )
}

export default ExpirationDatePicker
