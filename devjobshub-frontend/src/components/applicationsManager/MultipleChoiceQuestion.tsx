import { MultipleChoiceQuestionAndAnswer } from '@/types/multipleChoiceQuestionAndAnswer'

export const MultipleChoiceQuestion = ({question}: {question: MultipleChoiceQuestionAndAnswer}) => {
  return (
    <div className='flex flex-col gap-y-1'>
      <p className='font-bold'>{question.number+1}. {question.question}</p>
      <div className='flex flex-col'>
        {question.answers.map((element: number, i: number) => <p key={i}>{question.possibleAnswers.at(element)}</p>)}
      </div>
    </div>
  )
}
