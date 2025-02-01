import { MultipleChoiceQuestion } from "@/types/multipleChoiceQuestion";
import { Question } from "@/types/question";
import { QuestionWithType } from "@/types/questionWithType";
import { RadioQuestion } from "@/types/radioQuestion";

export const convertQuestionsListToListOfOpenQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<Question> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="question"){
            list.push({number: index+1, question: element.question} satisfies Question as Question)
        }
    })
    return list
}

export const convertQuestionsListToListOfRadioQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<RadioQuestion> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="radioQuestion"){
            list.push({number: index+1, question: element.question, possibleAnswers: element.possibleAnswers} as RadioQuestion)
        }
    })
    return list
}

export const convertQuestionsListToListOfMultipleChoiceQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<MultipleChoiceQuestion> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="multipleChoiceQuestion"){
            list.push({number: index+1, question: element.question, possibleAnswers: element.possibleAnswers} as MultipleChoiceQuestion)
        }
    })
    return list
}