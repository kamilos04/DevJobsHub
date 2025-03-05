import React, { useEffect } from 'react'
import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { DialogClose } from '@radix-ui/react-dialog'
import { Offer } from '@/types/offer'
import { User } from '@/types/user'
import * as yup from 'yup';
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import { useDispatch, useSelector } from 'react-redux'
import { addRecruiter, removeRecruiter } from '@/state/offer/action'
import { useToast } from '@/hooks/use-toast'
import { setFailNull, setSuccessNull } from '@/state/offer/offerSlice'

const ManageRecruitersDialog = ({ offer, dispatchRequest }: { offer: Offer, dispatchRequest: any }) => {
    const dispatch = useDispatch<any>()
    const offerStore = useSelector((store: any) => (store.offer))
    const {toast} = useToast()
    const emailSchema = yup.object().shape({
            email: yup.string().email('Invalid email').required('Email is required'),
        });
    
        const {
            register: registerForm,
            handleSubmit: handleSubmit,
            formState: { errors: formErrors },
            reset: resetForm
        } = useForm({
            resolver: yupResolver(emailSchema)
        })


    const handleAddRecruiterClick = (data: any) => {
        dispatch(addRecruiter({offerId: offer.id, userEmail: data.email}))

    }

    const handleRemoveRecruiterClick = (data: any) => {
        dispatch(removeRecruiter({offerId: offer.id, userEmail: data.email}))

    }


    useEffect(() => {
        if(offerStore.success === "addRecruiter"){
            dispatch(setSuccessNull())
            toast({
                variant: "default",
                className: "bg-green-800",
                title: "The recruiter has been added.",
            });
            resetForm({email: ""})
            dispatchRequest()
            
            
        }
        else if(offerStore.success === "removeRecruiter"){
            dispatch(setSuccessNull())
            toast({
                variant: "default",
                className: "bg-green-800",
                title: "The recruiter has been removed.",
            });
            resetForm({email: ""})
            dispatchRequest()
            
        }
    }, [offerStore.success])

    useEffect(() => {
        if(offerStore.fail === "addRecruiter"){
            dispatch(setFailNull())
            toast({
                variant: "destructive",
                title: "Something went wrong!",
            });
            resetForm({email: ""})
            
        }
        else if(offerStore.fail === "removeRecruiter"){
            dispatch(setFailNull())
            toast({
                variant: "destructive",
                title: "Something went wrong!",
            });
            resetForm({email: ""})
            
        }
    }, [offerStore.fail])


    return (
        <div>
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant="outline">Manage recruiters</Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[35rem]">
                    <DialogHeader>
                        <DialogTitle>Manage recruiters</DialogTitle>
                    </DialogHeader>
                    <div className='flex flex-col'>
                        <div className='flex flex-row items-start flex-wrap gap-x-2 gap-y-2 mb-10'>

                            {offer.recruiters?.map((element: User) => <Button className='cursor-text select-text' variant="outline" key={element.id} >{element.name} {element.surname} - {element.email}</Button>)}
                        </div>
                        <div className='flex flex-col gap-y-2'>
                            <Input type="email" placeholder="Enter user email" {...registerForm('email')}/>
                            <p className="text-red-500 text-sm font-normal">{formErrors.email?.message}</p>
                            <div className='flex flex-row gap-x-2'>
                                <Button variant="default" onClick={() => handleSubmit(handleAddRecruiterClick)()}>Add recruiter</Button>
                                <Button variant="default" onClick={() => handleSubmit(handleRemoveRecruiterClick)()}>Remove recruiter</Button>

                            </div>
                        </div>

                    </div>
                    <DialogFooter>
                        <DialogClose asChild>
                            <Button variant="secondary">Save changes</Button>
                        </DialogClose>
                    </DialogFooter>

                </DialogContent>
            </Dialog>
        </div>
    )
}

export default ManageRecruitersDialog
