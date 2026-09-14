import axios from 'axios'

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

api.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('patientLookupToken')

        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }

        return config
    },
    (error) => Promise.reject(error)
)

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            clearCredentials()
            window.location.href = '/login'
        }

        return Promise.reject(error)
    }
)

export function setCredentials(token, username, role) {
    sessionStorage.setItem('patientLookupToken', token)
    sessionStorage.setItem('patientLookupUsername', username)
    sessionStorage.setItem('patientLookupRole', role)
}

export function restoreCredentials() {
    const token = sessionStorage.getItem('patientLookupToken')
    const username = sessionStorage.getItem('patientLookupUsername')
    const role = sessionStorage.getItem('patientLookupRole')

    if (!token) {
        return null
    }

    return {
        token,
        username,
        role
    }
}

export function clearCredentials() {
    sessionStorage.removeItem('patientLookupToken')
    sessionStorage.removeItem('patientLookupUsername')
    sessionStorage.removeItem('patientLookupRole')
}

export const authApi = {
    login: (username, password) =>
        api.post('/auth/login', {username, password}),

    signup: (username, password) =>
        api.post('/auth/signup', {username, password})
}

export const patientApi = {
    getAll: (name = '', page = 0, size = 5) => api.get('/patients', {
            params: {
                ...(name ? { name } : {}),
                page,
                size
            }
        }),

    getById: (id) => api.get(`/patients/${id}`),

    create: (patient) => api.post('/patients', patient),

    update: (id, patient) => api.put(`/patients/${id}`, patient),

    remove: (id) => api.delete(`/patients/${id}`)
}

export const userApi = {
    getAll: () =>
        api.get('/users'),

    updateRole: (id, role) =>
        api.put(`/users/${id}/role`, {
            role
        })
}

export default api