import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { authApi, setCredentials } from '../services/api'

export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin123')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()

    setError('')
    setLoading(true)

    try {
      const response = await authApi.login(username, password)

      const {
        token,
        username: loggedInUsername,
        role
      } = response.data

      setCredentials(
        token,
        loggedInUsername,
        role
      )

      onLogin(loggedInUsername)

      navigate('/patients')
    } catch (error) {
      console.error('Login failed:', error)

      setError(
        'Unable to sign in. Check your credentials and make sure the API is running.'
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="login-page">
      <section className="login-panel-left">
        <div>
          <div className="d-flex align-items-center gap-2 mb-5">
            {/*<span className="brand-mark">+</span>*/}
            <span className="brand-mark" aria-hidden="true">
              <svg
                  width="28"
                  height="28"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
              >
                <path
                    d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.6l-1-1a5.5 5.5 0 0 0-7.8 7.8L12 21l8.8-8.6a5.5 5.5 0 0 0 0-7.8Z"
                    stroke="currentColor"
                    strokeWidth="1.8"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                />

                <path
                    d="M3.5 12h4l1.4-3 2.2 6 1.8-4 1.3 1h6.3"
                    stroke="currentColor"
                    strokeWidth="1.8"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                />
              </svg>
            </span>
            <span className="fw-bold fs-5">CarePlus</span>
          </div>

          <div className="eyebrow text-white-50 mb-3">
            Patient information portal
          </div>

          <h1>Simple, secure patient lookup.</h1>

          <p className="mt-4">
            A focused workspace for viewing patient information and managing
            records through a protected clinical API.
          </p>

          <div className="feature-list">
            <div className="feature-row">
              <span className="feature-dot" />
              Fast patient search by name or ID
            </div>

            <div className="feature-row">
              <span className="feature-dot" />
              Role-based access for employees and admins
            </div>

            <div className="feature-row">
              <span className="feature-dot" />
              Responsive interface for desktop and tablet
            </div>
          </div>
        </div>
      </section>

      <section className="login-panel-right">
        <div className="surface login-card">
          <div className="login-kicker mb-2">
            Welcome back
          </div>

          <h2 className="fw-bold mb-1">
            Sign in
          </h2>

          <p className="text-secondary mb-4">
            Use your application credentials to continue.
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
                autoComplete="current-password"
              />
            </div>

            <button
              className="btn btn-primary-custom w-100 py-2"
              disabled={loading}
            >
              {loading ? 'Signing in…' : 'Sign in'}
            </button>
          </form>

          <div className="text-center mt-4">
              <span className="text-secondary">
                Don't have an account?{' '}
              </span>
            <Link to="/signup" className="fw-semibold">
              Create account
            </Link>
          </div>

          <div className="demo-box mt-4">
            <div className="fw-bold text-dark mb-1">
              Demo access
            </div>

            <div>
              <strong>Admin:</strong> admin / admin123
            </div>

            <div>
              <strong>Employee:</strong> employee / employee123
            </div>
          </div>
        </div>
      </section>
    </main>
  )
}

