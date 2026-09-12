import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { patientApi } from '../services/api'

export default function PatientDetailsPage({ isAdmin }) {
  const { id } = useParams()
  const [patient, setPatient] = useState(null)
  const [error, setError] = useState('')
  const [deleting, setDeleting] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    patientApi.getById(id).then(r => setPatient(r.data)).catch(() => setError('Patient not found.'))
  }, [id])

  const remove = async () => {
    if (!confirm('Delete this patient? This action cannot be undone.')) return
    try {
      setDeleting(true)
      await patientApi.remove(id)
      navigate('/patients')
    } catch {
      setError('Unable to delete this patient. You may need ADMIN access.')
      setDeleting(false)
    }
  }

  if (error) return <main className="container-xl page-wrap"><div className="alert alert-danger surface">{error}</div></main>
  if (!patient) return <main className="container-xl page-wrap"><div className="surface p-5 text-center text-secondary">Loading patient record…</div></main>

  const initials = `${patient.firstName?.[0] || ''}${patient.lastName?.[0] || ''}`.toUpperCase()

  return (
    <main className="container-fluid px-4 page-wrap">
      <div className="container-xl">
        <div className="mb-3"><Link className="text-secondary small" to="/patients">← Back to patients</Link></div>
        <div className="surface overflow-hidden">
          <div className="detail-hero d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3">
            <div className="d-flex align-items-center gap-3">
              <div className="detail-avatar">{initials}</div>
              <div><div className="small opacity-75 mb-1">Patient record #{patient.patientId}</div><h1 className="detail-name">{patient.firstName} {patient.lastName}</h1><div className="detail-meta">Date of birth · {patient.dateOfBirth}</div></div>
            </div>
            <div className="d-flex gap-2">
              {isAdmin && <Link className="btn btn-light rounded-3 px-3" to={`/patients/${id}/edit`}>Edit</Link>}
              {isAdmin && <button className="btn btn-outline-light rounded-3 px-3" onClick={remove} disabled={deleting}>{deleting ? 'Deleting…' : 'Delete'}</button>}
            </div>
          </div>

          <div className="detail-grid">
            <div className="eyebrow mb-2">Patient information</div>
            <div className="row g-0">
              <div className="col-md-6 pe-md-4"><div className="detail-item"><div className="detail-label">First name</div><div className="detail-value">{patient.firstName}</div></div></div>
              <div className="col-md-6 ps-md-4"><div className="detail-item"><div className="detail-label">Last name</div><div className="detail-value">{patient.lastName}</div></div></div>
              <div className="col-md-6 pe-md-4"><div className="detail-item"><div className="detail-label">Date of birth</div><div className="detail-value">{patient.dateOfBirth}</div></div></div>
              <div className="col-md-6 ps-md-4"><div className="detail-item"><div className="detail-label">Email</div><div className="detail-value">{patient.email || 'Not provided'}</div></div></div>
              <div className="col-md-6 pe-md-4"><div className="detail-item"><div className="detail-label">Phone</div><div className="detail-value">{patient.phone || 'Not provided'}</div></div></div>
              <div className="col-md-6 ps-md-4"><div className="detail-item"><div className="detail-label">Address</div><div className="detail-value">{patient.address || 'Not provided'}</div></div></div>
            </div>
          </div>
        </div>
      </div>
    </main>
  )
}
