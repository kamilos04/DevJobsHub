import { MultipleChoiceQuestion } from "./multipleChoiceQuestion";
import { Question } from "./question";
import { RadioQuestion } from "./radioQuestion";

export interface CreateOfferRequest {
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
    localization: string;
    address: string;
    expirationDate: string;
    aboutProject: string;
    responsibilitiesText: string;
    responsibilities: string[];
    requirements: string[];
    niceToHave: string[];
    whatWeOffer: string[];
    requiredTechnologies: number[];
    niceToHaveTechnologies: number[];
    questions: Question[];
    radioQuestions: RadioQuestion[];
    multipleChoiceQuestions: MultipleChoiceQuestion[];
}

export const emptyCreateOfferRequest: CreateOfferRequest = {
    name: "",
    jobLevel: "",
    firmName: "",
    operatingMode: "",
    specialization: "",
    minSalaryUoP: null,
    maxSalaryUoP: null,
    isSalaryMonthlyUoP: null,
    minSalaryB2B: null,
    maxSalaryB2B: null,
    isSalaryMonthlyB2B: null,
    minSalaryUZ: null,
    maxSalaryUZ: null,
    isSalaryMonthlyUZ: null,
    localization: "",
    address: "",
    expirationDate: "",
    aboutProject: "",
    responsibilitiesText: "",
    responsibilities: [],
    requirements: [],
    niceToHave: [],
    whatWeOffer: [],
    requiredTechnologies: [],
    niceToHaveTechnologies: [],
    questions: [],
    radioQuestions: [],
    multipleChoiceQuestions: []
}