import { changePassword, fetchProfile } from '@/state/profile/action';
import { useEffect } from 'react'
import { CgProfile } from "react-icons/cg";
import { MdFavoriteBorder } from "react-icons/md";
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import { MdLogout } from "react-icons/md";
import { setFailNull, setProfileNull, setSuccessNull } from '@/state/profile/profileSlice';
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
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { ChangePasswordRequest } from '@/types/changePasswordRequest';
import { useToast } from '@/hooks/use-toast';
import { MAIN_URL } from '@/config/mainConfig';

const Navbar = () => {
    const navigate = useNavigate()
    const profileStore = useSelector((store: any) => (store.profile))
    const dispatch = useDispatch<any>()
    const {toast} = useToast()

    useEffect(() => {
        dispatch(fetchProfile())
    }, [])


    const logOut = () => {
        localStorage.removeItem("jwt")
        dispatch(setProfileNull())
        dispatch(setSuccessNull())
        dispatch(setFailNull())
        navigate(MAIN_URL)
    }

    const changePasswordSchema = yup.object().shape({
        password: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
        passwordRepeat: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required').oneOf([yup.ref('password')], 'Passwords must match')
    })

    const {
        register: registerForm,
        handleSubmit: handleSubmit,
        formState: { errors: formErrors },
        reset: resetForm
    } = useForm({
        resolver: yupResolver(changePasswordSchema),
    });

    const onChangePasswordSubmit = (data: any) => {
        let reqData: ChangePasswordRequest = {
            newPassword: ""
        }
        reqData.newPassword = data.password

        dispatch(changePassword(reqData))
    };


    useEffect(() => {
        if(profileStore.success === "changePassword"){
            logOut()
            navigate("/login")
        }
    }, [profileStore.success])


    useEffect(() => {
            if (profileStore.fail === "changePassword") {
                toast({
                    variant: "destructive",
                    title: "Something went wrong!"
                });
                dispatch(setFailNull())
            }
        }, [profileStore.fail])

    return (
        <div className='flex flex-row h-[4.5rem] bg-my-background border-b-2 border-s-stone-900 items-center pl-6 pr-6 justify-between'>
            <div className='cursor-pointer' onClick={() => navigate(MAIN_URL)}>
                <span className='fontLogo text-[1.5rem] select-none'><span className='text-blue-500'>Dev</span><span className='text-gray-400'>Jobs</span><span className='text-gray-500'>Hub</span></span>

            </div>
            <div className='flex flex-row gap-x-8 items-center'>
                {(profileStore.profile && profileStore.profile.isFirm === true) && <div className='flex flex-row items-center'>
                    <button className='p-2 hover:text-blue-300' onClick={() => navigate("/recruiter/manager")}>Recruiter panel</button>
                </div>}
                {(profileStore.profile && profileStore.profile.isAdmin === true) && <div className='flex flex-row items-center'>
                    <button className='p-2 hover:text-blue-300' onClick={() => navigate("/admin/offers")}>Offers manager - admin</button>
                </div>}
                <button onClick={() => navigate("/favourite")} className='hover:text-blue-300'>
                    <MdFavoriteBorder className='text-[2rem] ' />
                </button>
                <Popover>
                    <PopoverTrigger asChild>
                        <button className='hover:text-blue-300'>
                            <CgProfile className='text-[2rem]' />
                        </button>
                    </PopoverTrigger>
                    <PopoverContent className="w-80">
                        {profileStore.profile ? <div className='flex flex-col'>
                            <p className='text-lg font-bold'>Hi, {profileStore.profile.name}!</p>
                            <Dialog>
                                <DialogTrigger asChild>
                                    <Button type='button' variant={"link"} className='w-min p-0' onClick={() => resetForm()}>Change password</Button>
                                </DialogTrigger>
                                
                                    <DialogContent className="sm:max-w-[425px]">
                                        <DialogHeader>
                                            <DialogTitle>Change password</DialogTitle>
                                            <DialogDescription>
                                                Make sure you are entering the correct password. You will be logged out after changing it!
                                            </DialogDescription>
                                        </DialogHeader>
                                        <form onSubmit={handleSubmit(onChangePasswordSubmit)}>
                                            <div className="grid gap-4 py-4">
                                            <div className="space-y-1">
                                                <Label htmlFor="password">New password</Label>
                                                <Input id="password" type="password" {...registerForm('password')} />
                                                <p className="text-red-500 text-sm font-normal">{formErrors.password?.message}</p>
                                            </div>
                                            <div className="space-y-1">
                                                <Label htmlFor="passwordRepeat">Repeat new password</Label>
                                                <Input id="passwordRepeat" type="password" {...registerForm('passwordRepeat')} />
                                                <p className="text-red-500 text-sm font-normal">{formErrors.passwordRepeat?.message}</p>
                                            </div>
                                        </div>
                                        <DialogFooter>
                                            <Button type="submit">Change password</Button>
                                        </DialogFooter></form>
                                    </DialogContent>
                            </Dialog>

                            <div className='flex flex-row justify-end'>
                                <Button type='button' className='flex flex-row gap-x-1 w-28  mt-2' variant={'secondary'} onClick={() => logOut()}><MdLogout />Log out</Button>
                            </div>
                        </div> :
                            <div className='flex flex-col'>
                                <p className='text-lg font-bold'>Hello!</p>
                                <p className='text-base'>Log in to your account!</p>
                                <Button type='button' className='flex flex-row gap-x-1 w-28 mt-4' variant={'secondary'} onClick={() => navigate("/login")}>Log in</Button>
                            </div>}
                    </PopoverContent>
                </Popover>


            </div>
        </div>
    )
}

export default Navbar
