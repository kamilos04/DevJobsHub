export interface RegisterRequest {
    email: string,
    password: string,
    name: string,
    surname?: string | null
    isFirm: boolean
}