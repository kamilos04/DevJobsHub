import { Input } from "@/components/ui/input"
import { QuestionAndAnswer } from '@/types/questionAndAnswer'
import { QuestionAndAnswerWithType } from '@/types/questionAndAnswerWithType'

const OpenQuestion = ({question, setQuestionsList}: {question: QuestionAndAnswer, setQuestionsList: any}) => {
  const changeAnswer = (value: string) => {
    setQuestionsList((prevItems: Array<QuestionAndAnswerWithType>) =>
      prevItems.map((item) =>
        item.question.number === question.number ? {...item, question: {...question, answer: value}} : item
      )
    );
  }



  return (
    <div className='flex flex-col gap-y-2'>
      <p>{question.number+1}. {question.question}</p>
      <Input type="text" placeholder="Enter your answer" value={question.answer} onChange={(e) => changeAnswer(e.target.value)}/>
    </div>
  )
}

export default OpenQuestion
