import React from 'react'
import { Controller } from 'react-hook-form'
import { Label } from '../ui/label'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select'

const SelectJobLevel = ({ control }: any) => {
    return (
        <div>
            <Controller
                name="jobLevel"
                control={control}
                render={({ field }) => (
                    <>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="joblevel">Job level</Label>
                            <Select onValueChange={field.onChange} value={field.value}>
                                <SelectTrigger className="w-[15rem]" id="joblevel">
                                    <SelectValue placeholder="Select a job level" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Job levels</SelectLabel>
                                        <SelectItem value="TRAINEE">Trainee</SelectItem>
                                        <SelectItem value="JUNIOR">Junior</SelectItem>
                                        <SelectItem value="MID">Mid</SelectItem>
                                        <SelectItem value="SENIOR">Senior</SelectItem>
                                        <SelectItem value="MANAGER">Manager</SelectItem>
                                        <SelectItem value="CLEVEL">C-Level</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                        </div>
                    </>
                )}
            />
        </div>
    )
}

export default SelectJobLevel
