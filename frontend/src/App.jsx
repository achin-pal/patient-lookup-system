import { useState } from 'react'
import {
  Navigate,
  Route,
  Routes,
  useLocation
} from 'react-router-dom'

import NavBar from './components/NavBar'
import LoginPage from './pages/LoginPage'
import PatientListPage from './pages/PatientListPage'
import PatientDetailsPage from './pages/PatientDetailsPage'
import PatientFormPage from './pages/PatientFormPage'
import SignupPage from './pages/SignupPage'

import {
  restoreCredentials,
  clearCredentials
} from './services/api'

function Protected({ auth, children, adminOnly = false }) {
  const location = useLocation()

  if (!auth) {
    return (
      <Navigate to="/login" state={{ from: location }} replace />
    )
  }

  if (adminOnly && auth.role !== 'ADMIN') {
    return <Navigate to="/patients" replace />
  }

  return children
}

export default function App() {
  const [auth, setAuth] = useState(() => restoreCredentials())

  const isAdmin = auth?.role === 'ADMIN'

  const handleLogin = () => {
    const storedAuth = restoreCredentials()
    setAuth(storedAuth)
  }

  const handleLogout = () => {
    clearCredentials()
    setAuth(null)
  }

  return (
    <div className="app-shell">
      {auth && (
        <NavBar username={auth.username} isAdmin={isAdmin} onLogout={handleLogout}/>
      )}
      <Routes>
        <Route path="/login" element={ auth ? <Navigate to="/patients" replace /> : <LoginPage onLogin={handleLogin} /> } />
        <Route path="/signup" element={ auth ? <Navigate to="/patients" replace /> : <SignupPage />} />
        <Route path="/patients" element={<Protected auth={auth}> <PatientListPage isAdmin={isAdmin} /> </Protected>} />
        <Route path="/patients/new" element={ <Protected auth={auth} adminOnly> <PatientFormPage /> </Protected> } />
        <Route path="/patients/:id" element={<Protected auth={auth}> <PatientDetailsPage isAdmin={isAdmin} /> </Protected> } />
        <Route path="/patients/:id/edit"  element={ <Protected auth={auth} adminOnly> <PatientFormPage editing /> </Protected> } />
        <Route path="*" element={ <Navigate to={auth ? '/patients' : '/login'} replace />} />
      </Routes>
    </div>
  )
}

