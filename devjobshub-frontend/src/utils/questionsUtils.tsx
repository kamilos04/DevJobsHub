import { MultipleChoiceQuestion } from "@/types/multipleChoiceQuestion";
import { MultipleChoiceQuestionAndAnswer } from "@/types/multipleChoiceQuestionAndAnswer";
import { Question } from "@/types/question";
import { QuestionAndAnswer } from "@/types/questionAndAnswer";
import { QuestionAndAnswerWithType } from "@/types/questionAndAnswerWithType";
import { QuestionWithType } from "@/types/questionWithType";
import { RadioQuestion } from "@/types/radioQuestion";
import { RadioQuestionAndAnswer } from "@/types/radioQuestionAndAnswer";

export const convertQuestionsListToListOfOpenQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<Question> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="question"){
            list.push({number: index, question: element.question} satisfies Question as Question)
        }
    })
    return list
}

export const convertQuestionsListToListOfRadioQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<RadioQuestion> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="radioQuestion"){
            list.push({number: index, question: element.question, possibleAnswers: element.possibleAnswers} as RadioQuestion)
        }
    })
    return list
}

export const convertQuestionsListToListOfMultipleChoiceQuestions = (questions: Array<QuestionWithType>) => {
    let list: Array<MultipleChoiceQuestion> = []
    questions.forEach((element: QuestionWithType, index: number) => {
        if(element.type==="multipleChoiceQuestion"){
            list.push({number: index, question: element.question, possibleAnswers: element.possibleAnswers} as MultipleChoiceQuestion)
        }
    })
    return list
}





export const addOpenQuestionsToQuestionsList = (questions: Array<Question>, questionsList: Array<QuestionAndAnswerWithType>) => {
    questions.forEach((element: Question) => {
        const questionAndAnswer: QuestionAndAnswer = {number: element.number, question: element.question, answer: ""}
        const questionAndAnswerWithType: QuestionAndAnswerWithType = {question: questionAndAnswer, type: "question"}
        questionsList.push(questionAndAnswerWithType)
    })
}


export const addRadioQuestionsToQuestionsList = (questions: Array<RadioQuestion>, questionsList: Array<QuestionAndAnswerWithType>) => {
    questions.forEach((element: RadioQuestion) => {
        const radioQuestionAndAnswer: RadioQuestionAndAnswer = {question: element.question, number: element.number, possibleAnswers: element.possibleAnswers, answer: 0}
        const radioQuestionAndAnswerWithType: QuestionAndAnswerWithType = {question: radioQuestionAndAnswer, type: "radioQuestion"}
        questionsList.push(radioQuestionAndAnswerWithType)
    })
}

export const addMultipleChoiceQuestionsToQuestionsList = (questions: Array<MultipleChoiceQuestion>, questionsList: Array<QuestionAndAnswerWithType>) => {
    questions.forEach((element: MultipleChoiceQuestion) => {
        const multipleChoiceQuestionAndAnswer: MultipleChoiceQuestionAndAnswer = {question: element.question, number: element.number, possibleAnswers: element.possibleAnswers, answers: []}
        const multipleChoiceQuestionAndAnswerWithType: QuestionAndAnswerWithType = {question: multipleChoiceQuestionAndAnswer, type: "multipleChoiceQuestion"}
        questionsList.push(multipleChoiceQuestionAndAnswerWithType)
    })
}