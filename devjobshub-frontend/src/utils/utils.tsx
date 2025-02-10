import { Offer } from "@/types/offer"
import { calcDaysToExpirationDateFromString, calcSecondsToExpirationDateFromString } from "./dateUtils"

export const getExpirationDate = (offer: Offer) => {
    const days = calcDaysToExpirationDateFromString(offer?.expirationDate)
    const seconds = calcSecondsToExpirationDateFromString(offer?.expirationDate)
    if (days < 0) {
        return ("Offer expired")
    }
    else if (days === 0) {
        if (seconds > 0) {
            return ("Offer expires today")
        }
        return ("Offer expired")
    }
    else {
        if (days > 1) {
            return (`Offer valid for ${days} days`)
        }
        return (`Offer valid for ${days} day`)
    }
}

export const contractsStringFromOffer = (offer: Offer) => {
    let arrayStrings = []
    if (offer?.isSalaryMonthlyUoP !== null) arrayStrings.push("Employment contract")
    if (offer?.isSalaryMonthlyB2B !== null) arrayStrings.push("B2B")
    if (offer?.isSalaryMonthlyUZ !== null) arrayStrings.push("Order contract")
    let text = arrayStrings.join(", ")
    return text
}