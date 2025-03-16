import { Controller } from 'react-hook-form'
import { Label } from '../ui/label'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '../ui/select'

const SelectOperatingMode = ({ control, error }: any) => {
    return (
        <div>
            <Controller
                name="operatingMode"
                control={control}
                render={({ field }) => (
                    <>
                        <div className='flex flex-col space-y-2'>
                            <Label htmlFor="operatingmode">Operating mode</Label>
                            <Select onValueChange={field.onChange} value={field.value}>
                                <SelectTrigger className="w-[15rem]" id="operatingmode">
                                    <SelectValue placeholder="Select an operating mode" />
                                </SelectTrigger>
                                <SelectContent >
                                    <SelectGroup >
                                        <SelectLabel>Operating modes</SelectLabel>
                                        <SelectItem value="REMOTE">Remote</SelectItem>
                                        <SelectItem value="HYBRID">Hybrid</SelectItem>
                                        <SelectItem value="STATIONARY">Stationary</SelectItem>
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

export default SelectOperatingMode
