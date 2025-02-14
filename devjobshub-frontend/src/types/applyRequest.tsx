import { MultipleChoiceQuestionAndAnswer } from "./multipleChoiceQuestionAndAnswer";
import { QuestionAndAnswer } from "./questionAndAnswer";
import { RadioQuestionAndAnswer } from "./radioQuestionAndAnswer";

export interface ApplyRequest {
    cvUrl: string,
    questionsAndAnswers: QuestionAndAnswer[];
    radioQuestionsAndAnswers: RadioQuestionAndAnswer[];
    multipleChoiceQuestionsAndAnswers: MultipleChoiceQuestionAndAnswer[];
}

export const emptyApplyRequest: ApplyRequest = {
    cvUrl: "",
    questionsAndAnswers: [],
    radioQuestionsAndAnswers: [],
    multipleChoiceQuestionsAndAnswers: []
}