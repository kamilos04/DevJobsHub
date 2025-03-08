import { MAIN_URL } from '@/config/mainConfig'
import { fetchProfile } from '@/state/profile/action'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router'

export const useProfile = (loginNeeded: boolean, recruiterNeeded: boolean) => {
    const dispatch = useDispatch<any>()
    const profileStore = useSelector((store: any) => (store.profile))
    const navigate = useNavigate()

    const getProfile = () => {
        dispatch(fetchProfile())
    }


    useEffect(() => {
        if(profileStore.profile && recruiterNeeded === true){
            if(profileStore.profile.isFirm === false){
                navigate(MAIN_URL)
            }
        }
    }, [profileStore.profile])


    useEffect(() => {
        if(profileStore.fail === "fetchProfile" && loginNeeded === true){
            navigate("/login")
        }
    }, [profileStore.fail])


  return {getProfile, profileStore}
}
