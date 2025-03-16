import { MAIN_URL } from '@/config/mainConfig'
import { fetchProfile } from '@/state/profile/action'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router'

export const useProfile = (loginNeeded: boolean, recruiterNeeded: boolean, adminNeeded: boolean) => {
    const dispatch = useDispatch<any>()
    const profileStore = useSelector((store: any) => (store.profile))
    const navigate = useNavigate()

    const getProfile = () => {
        dispatch(fetchProfile())
    }


    useEffect(() => {
        if (profileStore.profile) {
            if(adminNeeded){
                if(profileStore.profile.isAdmin === false){
                    if(recruiterNeeded){
                        if(profileStore.profile.isFirm === false){
                            navigate(MAIN_URL)
                        }
                    }
                    else {
                        navigate(MAIN_URL)
                    }
                }
            }
            else if(recruiterNeeded === true && profileStore.profile.isFirm === false){
                navigate(MAIN_URL)
            }


            // if (profileStore.profile.isAdmin === false && adminNeeded === true && profileStore.profile.isFirm === false) {
            //     navigate(MAIN_URL)
            // }
            // else if (recruiterNeeded === true) {
            //     if (profileStore.profile.isFirm === false) {
            //         navigate(MAIN_URL)
            //     }
            // }
        }


    }, [profileStore.profile])


    useEffect(() => {
        if (profileStore.fail === "fetchProfile" && loginNeeded === true) {
            navigate("/login")
        }
    }, [profileStore.fail])


    return { getProfile, profileStore }
}
