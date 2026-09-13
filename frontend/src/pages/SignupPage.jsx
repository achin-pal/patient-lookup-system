import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { authApi } from '../services/api'

export default function SignupPage() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    const navigate = useNavigate()

    const submit = async (e) => {
        e.preventDefault()

        setError('')

        if (password !== confirmPassword) {
            setError('Passwords do not match.')
            return
        }

        if (password.length < 6) {
            setError('Password must be at least 6 characters.')
            return
        }

        setLoading(true)

        try {
            await authApi.signup(username.trim(), password)

            navigate('/login', {
                state: {
                    message: 'Account created successfully. Please sign in.'
                }
            })
        } catch (err) {
            if (err.response?.status === 409) {
                setError('Username already exists.')
            } else if (err.response?.status === 400) {
                setError(
                    err.response?.data?.message ||
                    'Unable to create account. Check the information provided.'
                )
            } else {
                setError(
                    'Unable to create account. Make sure the API is running.'
                )
            }
        } finally {
            setLoading(false)
        }
    }

    return (
        <main className="login-page">
            <section className="login-panel-left">
                <div>
                    <div className="d-flex align-items-center gap-2 mb-5">
                        <span className="brand-mark">+</span>

                        <span className="fw-bold fs-5">
              CareConnect
            </span>
                    </div>

                    <div className="eyebrow text-white-50 mb-3">
                        Patient information portal
                    </div>

                    <h1>
                        Create your CareConnect account.
                    </h1>

                    <p className="mt-4">
                        Register for secure access to patient lookup and clinical
                        information.
                    </p>

                    <div className="feature-list">
                        <div className="feature-row">
                            <span className="feature-dot" />
                            Secure JWT-based authentication
                        </div>

                        <div className="feature-row">
                            <span className="feature-dot" />
                            Employee access to patient records
                        </div>

                        <div className="feature-row">
                            <span className="feature-dot" />
                            Role-based access control
                        </div>
                    </div>
                </div>
            </section>

            <section className="login-panel-right">
                <div className="surface login-card">

                    <div className="login-kicker mb-2">
                        New account
                    </div>

                    <h2 className="fw-bold mb-1">
                        Sign up
                    </h2>

                    <p className="text-secondary mb-4">
                        Create an employee account to access the patient portal.
                    </p>

                    {error && (
                        <div className="alert alert-danger toast-error py-2 small">
                            {error}
                        </div>
                    )}

                    <form onSubmit={submit}>

                        <div className="mb-3">
                            <label className="form-label form-label-custom">
                                Username
                            </label>

                            <input
                                className="form-control form-control-custom"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                                autoComplete="username"
                                placeholder="Choose a username"
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label form-label-custom">
                                Password
                            </label>

                            <input
                                type="password"
                                className="form-control form-control-custom"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                autoComplete="new-password"
                                placeholder="Create a password"
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label form-label-custom">
                                Confirm password
                            </label>

                            <input
                                type="password"
                                className="form-control form-control-custom"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                                autoComplete="new-password"
                                placeholder="Confirm your password"
                            />
                        </div>

                        <button
                            className="btn btn-primary-custom w-100 py-2"
                            disabled={loading}
                        >
                            {loading ? 'Creating account…' : 'Create account'}
                        </button>
                    </form>

                    <div className="text-center mt-4">
            <span className="text-secondary">
              Already have an account?{' '}
            </span>

                        <Link to="/login" className="fw-semibold">
                            Sign in
                        </Link>
                    </div>

                </div>
            </section>
        </main>
    )
}