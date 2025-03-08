import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'
import { Checkbox } from "@/components/ui/checkbox"
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType'

const MultipleChoiceQuestion = ({ question, setQuestionsList }: { question: MultipleChoiceQuestionAndAnswer, setQuestionsList: any }) => {


  const changeAnswers = (index: number, value: boolean) => {
    setQuestionsList((prevItems: Array<QuestionAndAnswerWithType>) =>
      prevItems.map((item) =>
        item.question.number === question.number
          ? {
              ...item,
              question: {
                ...question,
                answers: value
                  ? question.answers.includes(index) ? question.answers : [...question.answers, index]
                  : question.answers.filter((el) => el !== index),
              },
            }
          : item
      )
    );
  };


  const isChecked = (index: number) => {
    if(question.answers.includes(index)){
      return true
    }
    return false
  }


  return (
    <div className='flex flex-col gap-y-2'>
      <p>{question.number + 1}. {question.question}</p>
      <div className='flex flex-col gap-y-2'>
        {question.possibleAnswers.map((element, index) =>
          <div className="flex items-center space-x-2" key={index}>
            <Checkbox id={`${question.number}-${index}`} checked={isChecked(index)} onCheckedChange={(value) => changeAnswers(index, value as boolean)} />
            <label
              htmlFor={`${question.number}-${index}`}
              className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            >
              {element}
            </label>
          </div>
        )}
      </div>


    </div>
  )
}

export default MultipleChoiceQuestion



