import { useState } from 'react'
import { Navigate, Route, Routes, useLocation } from 'react-router-dom'
import NavBar from './components/NavBar'
import LoginPage from './pages/LoginPage'
import PatientListPage from './pages/PatientListPage'
import PatientDetailsPage from './pages/PatientDetailsPage'
import PatientFormPage from './pages/PatientFormPage'
import { restoreCredentials } from './services/api'

function Protected({ username, children, adminOnly = false }) {
  const location = useLocation()
  if (!username) return <Navigate to="/login" state={{ from: location }} replace />
  if (adminOnly && username !== 'admin') return <Navigate to="/patients" replace />
  return children
}

export default function App() {
  const stored = restoreCredentials()
  const [username, setUsername] = useState(stored?.username || '')
  const isAdmin = username === 'admin'

  return (
    <div className="app-shell">
      {username && <NavBar username={username} isAdmin={isAdmin} />}
      <Routes>
        <Route path="/login" element={<LoginPage onLogin={setUsername} />} />
        <Route path="/patients" element={<Protected username={username}><PatientListPage isAdmin={isAdmin} /></Protected>} />
        <Route path="/patients/new" element={<Protected username={username} adminOnly><PatientFormPage /></Protected>} />
        <Route path="/patients/:id" element={<Protected username={username}><PatientDetailsPage isAdmin={isAdmin} /></Protected>} />
        <Route path="/patients/:id/edit" element={<Protected username={username} adminOnly><PatientFormPage editing /></Protected>} />
        <Route path="*" element={<Navigate to={username ? '/patients' : '/login'} replace />} />
      </Routes>
    </div>
  )
}
