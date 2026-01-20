import { useEffect, useState } from 'react'
import { getUsers } from '../../api/users_api'
import type { UserDTO } from '../../types/users'
import './users.css'

export default function Users() {
    const [users, setUsers] = useState<UserDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

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
            <h1>Users</h1>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : users.length === 0 ? (
                <div>No users found</div>
            ) : (
                <div className="users-list">
                    {users.map((u) => (
                        <div className="user-row" key={u.id}>
                            <div className="user-email">{u.email}</div>
                            <div className="user-role">{u.role}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}