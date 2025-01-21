import React, { useEffect, useState } from 'react'
import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs"
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import * as yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RegisterRequest } from '@/types/registerRequest'
import { useDispatch, useSelector } from 'react-redux'
import { login, register } from '@/state/profile/action'
import { useToast } from '@/hooks/use-toast'
import { LoginRequest } from '@/types/loginRequest'





const Login = () => {
    const [isFirm, setIsFirm] = useState<boolean | null>(null);
    const [surname, setSurname] = useState<string>("")
    const profile = useSelector((store: any) => store.profile)
    const { toast } = useToast()
    const dispatch = useDispatch<any>()


    const onLoginSubmit = (data: any) => {
        let reqData: LoginRequest = {
            email: "",
            password: ""
        }
        reqData.email = data.loginEmail
        reqData.password = data.loginPassword

        dispatch(login(reqData))
    };

    const onRegisterSubmit = (data: any) => {
        let reqData: RegisterRequest = {
            email: "",
            password: "",
            name: "",
            isFirm: false
        }
        reqData.email = data.registerEmail
        reqData.password = data.registerPassword
        reqData.name = data.name
        if (data.accountType === "firm") {
            reqData.isFirm = true
        }
        else {
            reqData.isFirm = false
            reqData.surname = data.surname
        }
        dispatch(register(reqData))
    };


    useEffect(() => {
        console.log(profile.success)
    }, [profile.success])

    const handleValueChangeAccountType = (data: any) => {
        if (data === "firm") {
            setIsFirm(true);
            if (surname === "") {
                setValueRegister("surname", " ")
                setSurname(" ")
            }

        }
        else {
            setIsFirm(false);
        }
    }


    useEffect(() => {
        if (profile.fail === "register") {
            if (profile.error === "Account already exists") {
                toast({
                    variant: "destructive",
                    title: "Account already exists!",
                    description: "If you already have an account, sign in."
                });
            } else {
                toast({
                    variant: "destructive",
                    title: "An error occurred!",
                    description: "Make sure you enter your details correctly."
                });
            }
        }
        else if(profile.fail === "login"){
            if (profile.error === "Invalid username or password") {
                toast({
                    variant: "destructive",
                    title: "Invalid email or password!",
                    description: "Check email and password."
                });
            } else {
                toast({
                    variant: "destructive",
                    title: "An error occurred!",
                    description: "Make sure you enter your details correctly."
                });
            }
        }
    }, [profile.fail])


    const loginSchema = yup.object().shape({
        loginEmail: yup.string().email("Invalid email").required("Email is required"),
        loginPassword: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
    })

    const registerSchema = yup.object().shape({
        accountType: yup.string().required('Account type is required'),
        name: yup.string().required('Name is required'),
        surname: yup.string().required('Surname is required'),
        registerEmail: yup.string().email('Invalid email').required('Email is required'),
        registerPassword: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
    });

    const {
        register: loginRegister,
        handleSubmit: handleLoginSubmit,
        formState: { errors: loginErrors }
    } = useForm({
        resolver: yupResolver(loginSchema)
    })

    const {
        register: registerForm,
        handleSubmit: handleRegisterSubmit,
        setValue: setValueRegister,
        formState: { errors: registerErrors }
    } = useForm({
        resolver: yupResolver(registerSchema),
    });



    return (
        <div className='h-screen flex flex-col items-center justify-center bg-my-background'>
            <Tabs defaultValue="login" className="w-[25rem]">
                <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="login">Sign in</TabsTrigger>
                    <TabsTrigger value="register">Register</TabsTrigger>
                </TabsList>
                <TabsContent value="login">
                    <form onSubmit={handleLoginSubmit(onLoginSubmit)}>
                        <Card className='flex flex-col items-center'>
                            <CardHeader>
                                <CardTitle>Sign in</CardTitle>
                            </CardHeader>
                            <div className='w-full'>
                                <CardContent className="space-y-4">
                                    <div className="space-y-1">
                                        <Label htmlFor="loginEmail">E-mail</Label>
                                        <Input id="loginEmail" placeholder='example@gmail.com' {...loginRegister('loginEmail')} />
                                        <p className="text-red-500 text-sm font-normal">{loginErrors.loginEmail?.message}</p>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="loginPassword">Password</Label>
                                        <Input id="loginPassword" type='password' {...loginRegister('loginPassword')} />
                                        <p className="text-red-500 text-sm font-normal">{loginErrors.loginPassword?.message}</p>
                                    </div>
                                </CardContent>
                            </div>

                            <CardFooter>
                                <Button type='submit'>Sign in</Button>
                            </CardFooter>
                        </Card>
                    </form>
                </TabsContent>
                <TabsContent value="register">
                    <form onSubmit={handleRegisterSubmit(onRegisterSubmit)}>
                        <Card className='flex flex-col items-center'>
                            <CardHeader className='flex flex-col items-center space-y-3'>
                                <CardTitle>Register</CardTitle>
                                <CardDescription>
                                    Please pay attention to the account type. You cannot change from a company account to a regular account and vice versa.
                                </CardDescription>
                            </CardHeader>
                            <div className='w-full'>
                                <CardContent className="space-y-2">

                                    <div className="space-y-1">
                                        <Label htmlFor="accountType">Account type</Label>
                                        <Select onValueChange={(value) => { handleValueChangeAccountType(value); setValueRegister("accountType", value) }}>
                                            <SelectTrigger className="w-full" id="accountType">
                                                <SelectValue placeholder="Select account type" />
                                            </SelectTrigger>
                                            <SelectContent>
                                                <SelectGroup>
                                                    <SelectItem value="user">User account</SelectItem>
                                                    <SelectItem value="firm">Company account</SelectItem>
                                                </SelectGroup>
                                            </SelectContent>
                                        </Select>
                                        <p className="text-red-500 text-sm font-normal">{registerErrors.accountType?.message}</p>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="name">Name</Label>
                                        <Input id="name" {...registerForm('name')} />
                                        <p className="text-red-500 text-sm font-normal">{registerErrors.name?.message}</p>
                                    </div>
                                    {!isFirm && <div className="space-y-1">
                                        <Label htmlFor="surname">Surname</Label>
                                        <Input id="surname" value={surname} onChange={(e) => {
                                            setSurname(e.target.value);
                                            setValueRegister("surname", e.target.value)
                                        }} />
                                        <p className="text-red-500 text-sm font-normal">{registerErrors.surname?.message}</p>
                                    </div>}
                                    <div className="space-y-1">
                                        <Label htmlFor="registerEmail">E-mail</Label>
                                        <Input id="registerEmail" placeholder='example@gmail.com' {...registerForm('registerEmail')} />
                                        <p className="text-red-500 text-sm font-normal">{registerErrors.registerEmail?.message}</p>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="registerPassword">New password</Label>
                                        <Input id="registerPassword" type="password" {...registerForm('registerPassword')} />
                                        <p className="text-red-500 text-sm font-normal">{registerErrors.registerPassword?.message}</p>
                                    </div>
                                </CardContent></div>
                            <CardFooter>
                                <Button type='submit'>Register</Button>
                            </CardFooter>
                        </Card>
                    </form>
                </TabsContent>

            </Tabs>
        </div >
    )
}

export default Login
