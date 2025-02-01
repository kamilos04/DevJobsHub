import { useState } from 'react'
import './App.css'


import MainRoute from './routers/MainRoute'
import { Toaster } from './components/ui/toaster'


function App() {
  return (
      <div className="App">
        <MainRoute/>
        <Toaster />
      </div>
  )
}

export default App
