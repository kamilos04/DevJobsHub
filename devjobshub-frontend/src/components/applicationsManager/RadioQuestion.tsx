import { RadioQuestionAndAnswer } from '@/types/radioQuestionAndAnswer'

export const RadioQuestion = ({question}: {question: RadioQuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold text-blue-300'>{question.number+1}. {question.question}</p>
      <ul className='list-disc pl-5'>
        <li>{question.possibleAnswers.at(question.answer)}</li>
      </ul>
      
    </div>
  )
}
