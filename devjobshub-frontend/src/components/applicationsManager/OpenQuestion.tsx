import { QuestionAndAnswer } from '@/types/questionAndAnswer'

export const OpenQuestion = ({question}: {question: QuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold text-blue-300'>{question.number+1}. {question.question}</p>
      <p>{question.answer}</p>
    </div>
  )
}
