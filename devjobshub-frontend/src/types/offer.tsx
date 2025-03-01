import { MultipleChoiceQuestion } from "./multipleChoiceQuestion";
import { Question } from "./question";
import { RadioQuestion } from "./radioQuestion";
import { Technology } from "./technology";

export interface Offer {
    id: number
    name: string;
    firmName: string
    jobLevel: string;
    operatingMode: string;
    specialization: string;
    minSalaryUoP: number | null;
    maxSalaryUoP: number | null;
    isSalaryMonthlyUoP: boolean | null;
    minSalaryB2B: number | null;
    maxSalaryB2B: number | null;
    isSalaryMonthlyB2B: boolean | null;
    minSalaryUZ: number | null;
    maxSalaryUZ: number | null;
    isSalaryMonthlyUZ: boolean | null;
    applications? : any | undefined
    favouriteApplications? : any | undefined
    localization: string;
    address: string;
    expirationDate: string;
    dateTimeOfCreation: string;
    likedByUsers? : any | undefined
    requiredTechnologies: Array<Technology>
    aboutProject: string;
    responsibilitiesText: string;
    responsibilities: string[];
    requirements: string[];
    niceToHave: string[];
    whatWeOffer: string[];
    niceToHaveTechnologies: Array<Technology>;
    questions: Question[];
    radioQuestions: RadioQuestion[];
    multipleChoiceQuestions: MultipleChoiceQuestion[];
    recruiters?: any | undefined
    isLiked: boolean | null,
    imageUrl: string | null
}