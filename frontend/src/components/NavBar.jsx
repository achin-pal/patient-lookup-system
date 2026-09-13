import { Link, useLocation, useNavigate } from 'react-router-dom'

export default function NavBar({ username, isAdmin, onLogout }) {
  const navigate = useNavigate()
  const location = useLocation()

  const initials = username.slice(0, 2).toUpperCase()

  const logout = () => {
    onLogout()
    navigate('/login')
  }

  return (
    <header className="app-navbar">
      <div className="container-fluid px-4 py-2">
        <div className="d-flex align-items-center justify-content-between gap-3">

          <div className="d-flex align-items-center gap-3">
            <Link
              to="/patients"
              className="d-flex align-items-center gap-2"
            >
              <span className="brand-mark">+</span>

              <span>
                <span className="d-block brand-title">
                  CareConnect
                </span>

                <span className="d-block brand-subtitle">
                  Patient Lookup
                </span>
              </span>
            </Link>

            <nav className="d-none d-md-flex ms-3 gap-1">
              <Link
                className={`nav-link-custom ${
  location.pathname === '/patients'
      ? 'active'
      : ''
}`}
                to="/patients"
              >
                Patients
              </Link>

              {isAdmin && (
                <Link
                  className={`nav-link-custom ${
  location.pathname.includes('/new')
      ? 'active'
      : ''
}`}
                  to="/patients/new"
                >
                  Add patient
                </Link>
              )}
            </nav>
          </div>

          <div className="d-flex align-items-center gap-2">
            <span className="user-pill">
              <span className="user-avatar">
                {initials}
              </span>

              {username}
              {isAdmin ? ' • Admin' : ' • Employee'}
            </span>

            <button
              className="btn btn-sm btn-outline-secondary rounded-pill px-3"
              onClick={logout}
            >
              Sign out
            </button>
          </div>

        </div>
      </div>
    </header>
  )
}

