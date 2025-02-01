import React from 'react'
import { Checkbox } from '../ui/checkbox'
import { Separator } from '../ui/separator'
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '../ui/select'
import { Label } from '../ui/label'
import { Input } from '../ui/input'
import { Controller } from 'react-hook-form'

const ContractType = ({ control, isContractCheckbox = {}, showSalaryCheckbox = {}, selectMonthlyOrHourly = {}, inputMinSalary = {}, inputMaxSalary = {}, errorMonthlyOrHourly, errorMin, errorMax, disabled }: any) => {
    // const handleCheckBox = (e: any) => {
    //     console.log(e)
    // }
    return (
        <div className='flex flex-col space-y-3 p-4 border-[1px] rounded-lg w-min bg-my-card'>
            <div className="flex items-center space-x-2">
                <Controller
                    name={isContractCheckbox.registerAs}
                    control={control}
                    defaultValue={false}
                    render={({ field }) => (
                        <>
                            <Checkbox {...field} onCheckedChange={field.onChange} checked={field.value} {...isContractCheckbox.props} id={isContractCheckbox.registerAs} />
                            <label
                                htmlFor={isContractCheckbox.registerAs}
                                className="text-sm leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 font-bold"
                            >
                                {isContractCheckbox.label}
                            </label>
                        </>
                    )}
                />

            </div>


            <Separator className="my-4" />
            <div className="flex items-center space-x-2">
                <Controller
                    name={showSalaryCheckbox.registerAs}
                    control={control}
                    defaultValue={false}
                    render={({ field }) => (
                        <>
                            <Checkbox {...field} onCheckedChange={field.onChange} checked={field.value} {...showSalaryCheckbox.props} id={showSalaryCheckbox.registerAs} disabled={disabled}/>
                            <label
                                htmlFor={showSalaryCheckbox.registerAs}
                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                                Show salary
                            </label>
                        </>
                    )}
                />

            </div>
            <Controller
                name={selectMonthlyOrHourly.registerAs}
                control={control}
                render={({ field }) => (
                    <div className='flex flex-col space-y-1'>
                        <Select {...selectMonthlyOrHourly.props} onValueChange={field.onChange} value={field.value} disabled={disabled}>
                            <SelectTrigger className="w-[14rem]">
                                <SelectValue placeholder="Monthly or hourly salary" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectGroup>
                                    <SelectItem value="monthly">Monthly</SelectItem>
                                    <SelectItem value="hourly">Hourly</SelectItem>
                                </SelectGroup>
                            </SelectContent>
                        </Select>
                        {errorMonthlyOrHourly && <p className="text-red-500 text-sm font-normal">{errorMonthlyOrHourly}</p>}
                    </div>
                )}
            ></Controller>


            <div className='flex flex-row space-x-3 items-center' >
                <div className="grid w-full max-w-sm items-center gap-2">
                    <Label >Minimum salary</Label>
                    <Input type="number" className='w-[8rem]' {...inputMinSalary.props} disabled={disabled} onKeyDown={(event) => {if (event.key === "Enter") {event.preventDefault();}}}/>
                    {errorMin && <p className="text-red-500 text-sm font-normal">{errorMin}</p>}
                </div>
                <div className="grid w-full max-w-sm items-center gap-2">
                    <Label >Maximum salary</Label>
                    <div className='flex flex-row items-center space-x-2'>
                        <Input type="number" className='w-[8rem]' {...inputMaxSalary.props} disabled={disabled} onKeyDown={(event) => {if (event.key === "Enter") {event.preventDefault();}}}/>
                        <span className='text-md'>PLN</span>

                    </div>
                    {errorMax && <p className="text-red-500 text-sm font-normal">{errorMax}</p>}
                </div>
            </div>
        </div>
    )
}

export default ContractType
