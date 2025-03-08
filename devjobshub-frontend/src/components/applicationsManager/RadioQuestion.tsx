import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer'

export const RadioQuestion = ({question}: {question: RadioQuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold'>{question.number+1}. {question.question}</p>
      <p>{question.possibleAnswers.at(question.answer)}</p>
    </div>
  )
}
