export interface User {
    id: number | undefined,
    name: string | undefined,
    surname: string | null | undefined,
    email: string | undefined,
    isBanned: boolean | undefined,
    isFirm: boolean | undefined,
    photoUrl: string | null | undefined,
    isAdmin: boolean | undefined,
    likedOffers?: any | undefined,
    applications?: any | undefined,
    offers?: any | undefined
}