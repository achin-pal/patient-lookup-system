import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' }
})

export function setCredentials(username, password) {
  api.defaults.auth = { username, password }
  sessionStorage.setItem('patientLookupAuth', btoa(`${username}:${password}`))
}

export function restoreCredentials() {
  const encoded = sessionStorage.getItem('patientLookupAuth')
  if (encoded) {
    const [username, password] = atob(encoded).split(':')
    api.defaults.auth = { username, password }
    return { username, password }
  }
  return null
}

export function clearCredentials() {
  delete api.defaults.auth
  sessionStorage.removeItem('patientLookupAuth')
}

export const patientApi = {
  getAll: (name = '') => api.get('/patients', { params: name ? { name } : {} }),
  getById: (id) => api.get(`/patients/${id}`),
  create: (patient) => api.post('/patients', patient),
  update: (id, patient) => api.put(`/patients/${id}`, patient),
  remove: (id) => api.delete(`/patients/${id}`)
}

export default api
