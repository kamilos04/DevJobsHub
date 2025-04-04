import { useEffect } from 'react'
import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { useDispatch, useSelector } from 'react-redux'
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup'
import { useForm } from 'react-hook-form'
import { CreateTechnologyRequest } from '@/types/createTechnologyRequest'
import { createTechnology } from '@/state/technology/action'
import { useToast } from '@/hooks/use-toast'
import { setFailNull, setSuccessNull } from '@/state/technology/technologySlice'

const CreateTechnologyDialog = (props: any) => {
    const dispatch = useDispatch<any>()
    const technologyStore = useSelector((store: any) => store.technology)
    const { toast } = useToast()

    const onSubmit = (data: any) => {
        let reqData: CreateTechnologyRequest = {
            name: ""
        }
        reqData.name = data.name
        dispatch(createTechnology(reqData))
    }

    useEffect(() => {
        if (technologyStore.success === "createTechnology") {
            toast({
                className: "bg-green-800",
                description: "The technology has been created, now you can find it in the search engine.",
            })
            props.setOpen(false)
            dispatch(setSuccessNull())
        }
    }, [technologyStore.success])

    useEffect(() => {
        if (technologyStore.fail === "createTechnology") {
            if (technologyStore.error === "Technology with this name already exists") {
                toast({
                    className: "bg-red-800",
                    description: "The technology already exists!",
                })

            }
            else {
                toast({
                    className: "bg-red-800",
                    description: "Something went wrong!",
                })
            }
            dispatch(setFailNull())
        }

    }, [technologyStore.fail])

    const createTechnologySchema = yup.object().shape({
        name: yup.string().required('Name is required')
    });

    const {
        register: formRegister,
        handleSubmit: handleCreateTechnologySubmit,
        formState: { errors: createTechnologyErrors }
    } = useForm({
        resolver: yupResolver(createTechnologySchema)
    })

    return (
        <Dialog open={props.open} onOpenChange={props.setOpen}>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Create technology</DialogTitle>
                    <DialogDescription>
                        Make sure you spell it correctly because this name will show up in search engine.
                    </DialogDescription>
                </DialogHeader>
                    <div >
                        <Input
                            id="technologyName"
                            placeholder='Example name'
                            className="col-span-3"
                            {...formRegister('name')}
                            onKeyDown={(event) => {if (event.key === "Enter") {event.preventDefault();}}}
                        />
                        <p className="text-red-500 text-sm font-normal mt-1">{createTechnologyErrors.name?.message}</p>
                    </div>
                    <DialogFooter>
                        <Button type="button" className='mt-4' onClick={handleCreateTechnologySubmit(onSubmit)}>Create technology</Button>
                    </DialogFooter>
            </DialogContent>
        </Dialog>

    )
}

export default CreateTechnologyDialog
