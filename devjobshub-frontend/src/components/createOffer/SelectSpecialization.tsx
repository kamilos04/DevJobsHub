import React from 'react'
import { Controller } from 'react-hook-form'
import { Label } from '../ui/label'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select'

const SelectSpecialization = ({ control, error }: any) => {
    return (
        <div>
            <Controller
                name="specialization"
                control={control}
                render={({ field }) => (
                    <>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="specialization">Specialization</Label>
                            <Select onValueChange={field.onChange} value={field.value}>
                                <SelectTrigger className="w-[15rem]" id="specialization">
                                    <SelectValue placeholder="Select a specialization" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Specializations</SelectLabel>
                                        <SelectItem value="BACKEND">Backend</SelectItem>
                                        <SelectItem value="FRONTEND">Frontend</SelectItem>
                                        <SelectItem value="FULLSTACK">Fullstack</SelectItem>
                                        <SelectItem value="MOBILE">Mobile</SelectItem>
                                        <SelectItem value="EMBEDDED">Embedded</SelectItem>
                                        <SelectItem value="ARCHITECTURE">Architecture</SelectItem>
                                        <SelectItem value="TESTING">Testing</SelectItem>
                                        <SelectItem value="SECURITY">Security</SelectItem>
                                        <SelectItem value="AIML">AI/ML</SelectItem>
                                        <SelectItem value="DATASCIENCE">Data Science</SelectItem>
                                        <SelectItem value="HELPDESK">Helpdesk</SelectItem>
                                        <SelectItem value="UXUI">UX/UI</SelectItem>
                                        <SelectItem value="ADMINISTRATOR">Administrator</SelectItem>
                                        <SelectItem value="PROJECTMANAGER">Project Manager</SelectItem>
                                        <SelectItem value="OTHER">Other</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                            {error && <p className="text-red-500 text-sm font-normal">{error}</p>}
                        </div>
                    </>
                )}
            />
        </div>
    )
}

export default SelectSpecialization
