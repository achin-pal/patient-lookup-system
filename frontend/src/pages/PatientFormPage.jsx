import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { patientApi } from '../services/api'

const empty = { firstName: '', lastName: '', dateOfBirth: '', email: '', phone: '', address: '' }

export default function PatientFormPage({ editing = false }) {
  const { id } = useParams()
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    if (editing) patientApi.getById(id).then(r => setForm(r.data)).catch(() => setError('Patient not found.'))
  }, [editing, id])

  const change = e => setForm({ ...form, [e.target.name]: e.target.value })

  const submit = async e => {
    e.preventDefault()
    setError('')
    setSaving(true)
    try {
      if (editing) await patientApi.update(id, form)
      else await patientApi.create(form)
      navigate(editing ? `/patients/${id}` : '/patients')
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to save patient. You may need ADMIN access.')
      setSaving(false)
    }
  }

  return (
    <main className="container-fluid px-4 page-wrap">
      <div className="container-xl" style={{ maxWidth: 940 }}>
        <div className="mb-3"><button className="btn text-secondary p-0" onClick={() => navigate(-1)}>← Back</button></div>
        <div className="mb-4"><div className="eyebrow">Patient management</div><h1 className="page-title">{editing ? 'Edit patient' : 'Add a new patient'}</h1><p className="page-subtitle">{editing ? 'Update the details below and save the patient record.' : 'Create a patient record using the information available to your team.'}</p></div>

        <div className="surface form-shell">
          {error && <div className="alert alert-danger">{error}</div>}
          <form onSubmit={submit}>
            <div className="form-section-title">Personal details</div>
            <div className="row g-3">
              <div className="col-md-6"><label className="form-label form-label-custom">First name</label><input name="firstName" className="form-control form-control-custom" value={form.firstName} onChange={change} required /></div>
              <div className="col-md-6"><label className="form-label form-label-custom">Last name</label><input name="lastName" className="form-control form-control-custom" value={form.lastName} onChange={change} required /></div>
              <div className="col-md-6"><label className="form-label form-label-custom">Date of birth</label><input type="date" name="dateOfBirth" className="form-control form-control-custom" value={form.dateOfBirth} onChange={change} required /></div>
            </div>

            <hr className="my-4" />
            <div className="form-section-title">Contact details</div>
            <div className="row g-3">
              <div className="col-md-6"><label className="form-label form-label-custom">Email</label><input type="email" name="email" className="form-control form-control-custom" value={form.email || ''} onChange={change} /></div>
              <div className="col-md-6"><label className="form-label form-label-custom">Phone</label><input name="phone" className="form-control form-control-custom" value={form.phone || ''} onChange={change} /></div>
              <div className="col-12"><label className="form-label form-label-custom">Address</label><textarea name="address" className="form-control form-control-custom" rows="3" value={form.address || ''} onChange={change} /></div>
            </div>

            <div className="d-flex flex-column flex-sm-row justify-content-end gap-2 mt-4">
              <button type="button" className="btn btn-light border rounded-3 px-4" onClick={() => navigate(-1)}>Cancel</button>
              <button className="btn btn-primary-custom rounded-3 px-4" disabled={saving}>{saving ? 'Saving…' : editing ? 'Save changes' : 'Create patient'}</button>
            </div>
          </form>
        </div>
      </div>
    </main>
  )
}
