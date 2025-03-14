import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'

export const MultipleChoiceQuestion = ({question}: {question: MultipleChoiceQuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold text-blue-300'>{question.number+1}. {question.question}</p>
        <ul className='list-disc pl-5'>
          {question.answers.map((element: number, i: number) => <li key={i}>{question.possibleAnswers.at(element)}</li>)}
        </ul>
        
      </div>
  )
}
