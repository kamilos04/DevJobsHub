import { useState } from 'react'
import './App.css'


import HomeRoute from './routers/HomeRoute'
import { Toaster } from './components/ui/toaster'


function App() {
  return (
      <div className="App">
        <HomeRoute/>
        <Toaster />
      </div>
  )
}

export default App
