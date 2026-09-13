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

              <span>
                <span className="d-block brand-title">
                  CarePlus
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

              {isAdmin && (
                  <Link
                      className={`nav-link-custom ${
                          location.pathname === '/users'
                              ? 'active'
                              : ''
                      }`}
                      to="/users"
                  >
                    Users
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

