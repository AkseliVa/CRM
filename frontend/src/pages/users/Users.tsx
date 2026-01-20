import { useEffect, useState } from 'react'
import { getUsers } from '../../api/users_api'
import type { UserDTO } from '../../types/users'
import './users.css'
import { useNavigate } from 'react-router-dom'

export default function Users() {
    const [users, setUsers] = useState<UserDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

    useEffect(() => {
        setLoading(true)
        getUsers()
            .then((data) => setUsers(data ?? []))
            .catch((err) => {
                console.error('Failed to load users', err)
                setError('Failed to load users')
            })
            .finally(() => setLoading(false))
    }, [])

    return (
        <div className="users-root">
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h1>Users</h1>
                <button onClick={() => navigate('/users/new')}>Create User</button>
            </div>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : users.length === 0 ? (
                <div>No users found</div>
            ) : (
                <div className="users-list">
                    {users.map((u) => (
                        <div
                            className="user-row"
                            key={u.id}
                            onClick={() => navigate(`/users/${u.id}`)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div className="user-email">{u.email}</div>
                            <div className="user-role">{u.role}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}