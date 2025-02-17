import { MultipleChoiceQuestionAndAnswer } from "./multipleChoiceQuestionAndAnswer";
import { QuestionAndAnswer } from "./questionAndAnswer";
import { RadioQuestionAndAnswer } from "./radioQuestionAndAnswer";

export interface Application {
    id: number,
    user: any,
    offer: any,
    cvUrl: string,
    dateTimeOfCreation: string,
    status: string,
    questionsAndAnswers: Array<QuestionAndAnswer>,
    radioQuestionsAndAnswers: Array<RadioQuestionAndAnswer>
    multipleChoiceQuestionsAndAnswers: Array<MultipleChoiceQuestionAndAnswer>
}