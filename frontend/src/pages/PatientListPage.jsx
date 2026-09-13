import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { patientApi } from '../services/api'

export default function PatientListPage({ isAdmin }) {
  const [patients, setPatients] = useState([])
  const [name, setName] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  const load = async () => {
    try {
      setLoading(true)
      setError('')

      const res = await patientApi.getAll(name.trim())

      setPatients(res.data)
    } catch (err) {
      if (err.response?.status === 403) {
        setError('You do not have permission to access patient records.')
      } else if (err.response?.status === 401) {
        setError('Your session has expired. Please sign in again.')
      } else {
        setError('Could not load patients. Make sure the API is running.')
      }
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  return (
    <main className="container-fluid px-4 page-wrap">
      <div className="container-xl">

        <div className="d-flex flex-column flex-lg-row align-items-lg-end justify-content-between gap-3 mb-4">
          <div>
            <div className="eyebrow">
              Clinical workspace
            </div>

            <h1 className="page-title">
              Patients
            </h1>

            <p className="page-subtitle">
              Search, review and manage patient records from one place.
            </p>
          </div>

          {isAdmin && (
            <Link
              className="btn btn-primary-custom rounded-3 px-4 py-2"
              to="/patients/new"
            >
              + Add patient
            </Link>
          )}
        </div>

        <div className="row g-3 mb-4">

          <div className="col-sm-6 col-lg-4">
            <div className="surface stat-card h-100 d-flex align-items-center justify-content-between">
              <div>
                <div className="stat-label">
                  Records found
                </div>

                <div className="stat-value">
                  {patients.length}
                </div>
              </div>

              <span className="stat-icon">
                ID
              </span>
            </div>
          </div>

          <div className="col-sm-6 col-lg-4">
            <div className="surface stat-card h-100 d-flex align-items-center justify-content-between">
              <div>
                <div className="stat-label">
                  Access level
                </div>

                <div className="stat-value fs-5">
                  {isAdmin ? 'Administrator' : 'Employee'}
                </div>
              </div>

              <span className="stat-icon">
                ✓
              </span>
            </div>
          </div>

        </div>

        <div className="surface search-shell mb-3">
          <span className="search-icon">
            ⌕
          </span>

          <input
            className="form-control"
            placeholder="Search by first or last name…"
            value={name}
            onChange={(e) => setName(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && load()}
          />

          <button
            className="btn btn-primary-custom rounded-3 px-3"
            onClick={load}
            disabled={loading}
          >
            {loading ? 'Loading…' : 'Search'}
          </button>
        </div>

        {error && (
          <div className="alert alert-danger surface py-3">
            {error}
          </div>
        )}

        <div className="surface table-shell">

          <div className="px-3 px-md-4 py-3 border-bottom d-flex justify-content-between align-items-center">
            <div>
              <div className="fw-bold text-dark">
                Patient directory
              </div>

              <div className="small text-secondary">
                {loading
                  ? 'Loading records…'
                  : `${patients.length} patient${patients.length === 1 ? '' : 's'}`
                }
              </div>
            </div>
          </div>

          <div className="table-responsive">
            <table className="table align-middle">

              <thead>
                <tr>
                  <th>ID</th>
                  <th>Patient</th>
                  <th>Date of birth</th>
                  <th>Email</th>
                  <th className="text-end">
                    Action
                  </th>
                </tr>
              </thead>

              <tbody>

                {!loading && patients.map((p) => (
                  <tr key={p.patientId}>

                    <td>
                      <span className="patient-id">
                        #{p.patientId}
                      </span>
                    </td>

                    <td>
                      <span className="patient-name">
                        {p.firstName} {p.lastName}
                      </span>
                    </td>

                    <td>
                      {p.dateOfBirth}
                    </td>

                    <td>
                      {p.email || (
                        <span className="text-secondary">
                          Not provided
                        </span>
                      )}
                    </td>

                    <td className="text-end">
                      <Link
                        className="btn btn-sm btn-outline-primary rounded-3 px-3"
                        to={`/patients/${p.patientId}`}
                      >
                        View record
                      </Link>
                    </td>

                  </tr>
                ))}

                {!loading && !patients.length && (
                  <tr>
                    <td colSpan="5">
                      <div className="empty-state">

                        <div className="empty-state-icon">
                          ⌕
                        </div>

                        <div className="fw-bold text-dark">
                          No patients found
                        </div>

                        <div className="small text-secondary">
                          Try another name or clear your search.
                        </div>

                      </div>
                    </td>
                  </tr>
                )}

              </tbody>

            </table>
          </div>
        </div>
      </div>
    </main>
  )
}
