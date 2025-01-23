import React from 'react'
import { Checkbox } from '../ui/checkbox'
import { Separator } from '../ui/separator'
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '../ui/select'
import { Label } from '../ui/label'
import { Input } from '../ui/input'

const ContractType = ({isContractCheckboxProps= {}, showSalaryCheckBoxProps = {}, selectMonthlyOrHourlyProps = {}, minSalaryProps = {}, maxSalaryProps = {}}: any) => {
    
    return (
        <div className='flex flex-col space-y-3 p-3 border-[1px] rounded-lg w-min'>
            <div className="flex items-center space-x-2">
                <Checkbox {...isContractCheckboxProps} />
                <label
                    htmlFor={isContractCheckboxProps.id}
                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                >
                    {isContractCheckboxProps.label}
                </label>

            </div>
            <Separator className="my-4" />
            <div className="flex items-center space-x-2">
                <Checkbox {...showSalaryCheckBoxProps} />
                <label
                    htmlFor={showSalaryCheckBoxProps.id}
                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                >
                    Show salary
                </label>

            </div>
            <Select {...selectMonthlyOrHourlyProps}>
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
            <div className='flex flex-row space-x-3 items-center' >
                <div className="grid w-full max-w-sm items-center gap-2">
                    <Label >Minimum salary</Label>
                    <Input type="number" className='w-[8rem]' {...minSalaryProps}/>
                </div>
                <div className="grid w-full max-w-sm items-center gap-2">
                    <Label >Maximum salary</Label>
                    <div className='flex flex-row items-center space-x-2'>
                        <Input type="number" className='w-[8rem]' {...minSalaryProps}/>
                        <span className='text-md'>PLN</span>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default ContractType
