import { MultipleChoiceQuestionAndAnswer } from "./multipleChoiceQuestionAndAnswer";
import { QuestionAndAnswer } from "./questionAndAnswer";
import { RadioQuestionAndAnswer } from "./radioQuestionAndAnswer";

export interface QuestionAndAnswerWithType {
    question: QuestionAndAnswer | RadioQuestionAndAnswer | MultipleChoiceQuestionAndAnswer,
    type: string
}