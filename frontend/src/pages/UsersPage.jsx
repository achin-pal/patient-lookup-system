import { useEffect, useState } from 'react'
import {
    restoreCredentials,
    userApi
} from '../services/api'

export default function UsersPage() {
    const [users, setUsers] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')

    const auth = restoreCredentials()
    const currentUsername = auth?.username

    const loadUsers = async () => {
        try {
            setLoading(true)
            setError('')

            const response = await userApi.getAll()
            setUsers(response.data)
        } catch {
            setError('Unable to load application users.')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        loadUsers()
    }, [])

    const changeRole = async (userId, role) => {
        try {
            setError('')

            const response = await userApi.updateRole(
                userId,
                role
            )

            setUsers((currentUsers) =>
                currentUsers.map((user) =>
                    user.id === userId
                        ? response.data
                        : user
                )
            )
        } catch (err) {
            if (err.response?.status === 403) {
                setError(
                    'You cannot change your own access level.'
                )
                return
            }

            setError('Unable to update access level.')
        }
    }

    return (
        <main className="container-fluid px-4 page-wrap">
            <div className="container-xl">

                <div className="mb-4">
                    <div className="eyebrow">
                        Administration
                    </div>

                    <h1 className="page-title">
                        User access
                    </h1>

                    <p className="page-subtitle">
                        Review application users and manage their access levels.
                    </p>
                </div>

                {error && (
                    <div className="alert alert-danger">
                        {error}
                    </div>
                )}

                <div className="surface table-shell">

                    <div className="px-4 py-3 border-bottom">
                        <div className="fw-bold text-dark">
                            Application users
                        </div>

                        <div className="small text-secondary">
                            {loading
                                ? 'Loading users…'
                                : `${users.length} registered users`
                            }
                        </div>
                    </div>

                    <div className="table-responsive">
                        <table className="table align-middle">

                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Access level</th>
                                <th className="text-end">
                                    Change access
                                </th>
                            </tr>
                            </thead>

                            <tbody>

                            {!loading && users.map((user) => {
                                const isCurrentUser =
                                    user.username === currentUsername

                                return (
                                    <tr key={user.id}>

                                        <td>
                                            #{user.id}
                                        </td>

                                        <td>
                                            <strong>
                                                {user.username}
                                            </strong>

                                            {isCurrentUser && (
                                                <span className="badge current-user-badge ms-2">
                                                    You
                                                </span>
                                            )}
                                        </td>

                                        <td>
                                            <span
                                                className={
                                                    user.role === 'ADMIN'
                                                        ? 'badge bg-primary'
                                                        : 'badge bg-secondary'
                                                }
                                            >
                                                {user.role}
                                            </span>
                                        </td>

                                        <td className="text-end">

                                            <select
                                                className="form-select form-select-sm d-inline-block"
                                                style={{ width: '150px' }}
                                                value={user.role}
                                                disabled={isCurrentUser}
                                                title={
                                                    isCurrentUser
                                                        ? 'You cannot change your own access level'
                                                        : 'Change user access level'
                                                }
                                                onChange={(e) =>
                                                    changeRole(
                                                        user.id,
                                                        e.target.value
                                                    )
                                                }
                                            >
                                                <option value="EMPLOYEE">
                                                    Employee
                                                </option>

                                                <option value="ADMIN">
                                                    Administrator
                                                </option>
                                            </select>

                                            {isCurrentUser && (
                                                <div className="small text-secondary mt-1">
                                                    Your access cannot be changed
                                                </div>
                                            )}

                                        </td>

                                    </tr>
                                )
                            })}

                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </main>
    )
}