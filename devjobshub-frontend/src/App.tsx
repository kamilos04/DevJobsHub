import { useState } from 'react'
import './App.css'

import { Button } from './components/ui/button'


function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div className="card">
        <Button variant="outline" onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </Button>
        <Button variant="destructive">Clickfdf me</Button>
      </div>
    </>
  )
}

export default App
