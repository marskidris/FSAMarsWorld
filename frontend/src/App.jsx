import { useState } from 'react'
import './App.css'
import AttributeForm from './components/AttributeForm'
import ResultsDisplay from './components/ResultsDisplay'
import { api } from './services/api'

function App() {
  const [results, setResults] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [activeTab, setActiveTab] = useState('compute')

  const handleCompute = async (formData) => {
    setLoading(true)
    setError(null)

    try {
      // Prepare the SheetInput for the compute endpoint
      const sheetInput = {
        attrs: {
          power: formData.power,
          vitality: formData.vitality,
          endurance: formData.endurance,
          mind: formData.mind,
          self: formData.self,
          intent: formData.intent,
          divine: formData.divine,
          force: formData.force,
          soul: formData.soul,
          spirit: formData.spirit
        },
        role: formData.role,
        aspect: formData.aspect,
        flavor: formData.flavor,
        fullTank: formData.fullTank,
        formulaVersion: formData.formulaVersion,
        beyondMortal: formData.beyondMortal
      }

      const result = await api.compute(sheetInput)
      setResults(result)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleExample = async (formData) => {
    setLoading(true)
    setError(null)

    try {
      const params = {
        beyondMortal: formData.beyondMortal,
        characterType: formData.characterType,
        allowNonSentient: formData.allowNonSentient
      }

      const result = await api.getExample(params)
      setResults(result)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleTrySphere = async (formData) => {
    setLoading(true)
    setError(null)

    try {
      const params = {
        power: formData.power,
        vitality: formData.vitality,
        endurance: formData.endurance,
        mind: formData.mind,
        self: formData.self,
        intent: formData.intent,
        divine: formData.divine,
        force: formData.force,
        soul: formData.soul,
        spirit: formData.spirit,
        role: formData.role,
        aspect: formData.aspect,
        formulaVersion: formData.formulaVersion,
        beyondMortal: formData.beyondMortal,
        characterType: formData.characterType,
        allowNonSentient: formData.allowNonSentient
      }

      const result = await api.trySphere(params)
      setResults(result)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const getSubmitHandler = () => {
    switch (activeTab) {
      case 'compute':
        return handleCompute
      case 'example':
        return handleExample
      case 'trySphere':
        return handleTrySphere
      default:
        return handleCompute
    }
  }

  return (
    <div className="App">
      <header className="App-header">
        <h1>FSA Mars World - Prime System Calculator</h1>
        <p>Calculate character prime systems using advanced Mars World algorithms</p>
      </header>

      <nav className="tab-navigation">
        <button
          className={activeTab === 'compute' ? 'tab active' : 'tab'}
          onClick={() => setActiveTab('compute')}
        >
          Compute Prime
        </button>
        <button
          className={activeTab === 'example' ? 'tab active' : 'tab'}
          onClick={() => setActiveTab('example')}
        >
          System Comparison
        </button>
        <button
          className={activeTab === 'trySphere' ? 'tab active' : 'tab'}
          onClick={() => setActiveTab('trySphere')}
        >
          Try Sphere System
        </button>
      </nav>

      <main className="main-content">
        <div className="content-grid">
          <div className="form-section">
            <AttributeForm
              onSubmit={getSubmitHandler()}
              loading={loading}
            />
          </div>

          <div className="results-section">
            <ResultsDisplay
              results={results}
              loading={loading}
              error={error}
            />
          </div>
        </div>
      </main>
    </div>
  )
}

export default App
