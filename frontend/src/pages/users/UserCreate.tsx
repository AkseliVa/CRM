import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createUser } from '../../api/users_api'
import type { UserCreateDTO, Role } from '../../types/users'
import '../shared/form.css'

export default function UserCreate() {
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState<Role>('USER')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setError(null)
    const payload: UserCreateDTO = { email, password, role }
    try {
      await createUser(payload)
      navigate('/users')
    } catch (err) {
      console.error(err)
      setError('Failed to create user')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="form-root">
      <h1>Create User</h1>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit} className="form">
        <label>
          Email
          <input value={email} onChange={(e) => setEmail(e.target.value)} required type="email" />
        </label>
        <label>
          Password
          <input value={password} onChange={(e) => setPassword(e.target.value)} required type="password" minLength={8} />
        </label>
        <label>
          Role
          <select value={role} onChange={(e) => setRole(e.target.value as Role)}>
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </label>

        <div className="form-actions">
          <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          <button type="button" onClick={() => navigate('/users')}>Cancel</button>
        </div>
      </form>
    </div>
  )
}
