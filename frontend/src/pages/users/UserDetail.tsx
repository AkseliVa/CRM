import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getUser, updateUser, deleteUser } from '../../api/users_api'
import { getTickets } from '../../api/tickets_api'
import type { UserDTO, UserUpdateDTO } from '../../types/users'
import type { TicketDTO } from '../../types/tickets'
import '../shared/form.css'

export default function UserDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [user, setUser] = useState<UserDTO | null>(null)
  const [editMode, setEditMode] = useState(false)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState<'ADMIN' | 'USER'>('USER')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [userTickets, setUserTickets] = useState<TicketDTO[]>([])
  const [ticketsLoading, setTicketsLoading] = useState(false)

  useEffect(() => {
    if (!id) return
    getUser(Number(id)).then((u) => {
      setUser(u)
      setEmail(u.email)
      setRole(u.role)
      setLoading(false)
    }).catch(()=>setLoading(false))
  }, [id])

  useEffect(() => {
    if (!id) return
    setTicketsLoading(true)
    getTickets()
      .then((all) => {
        const list = (all || []).filter((t: TicketDTO) => t.assignedUserId === Number(id))
        setUserTickets(list)
      })
      .catch(() => {})
      .finally(() => setTicketsLoading(false))
  }, [id])

  const onSave = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!id) return
    setSaving(true)
    const payload: UserUpdateDTO = { email, password, role }
    try {
      const updated = await updateUser(Number(id), payload)
      setUser(updated)
      setEditMode(false)
    } catch (err) { console.error(err) } finally { setSaving(false) }
  }

  const onDelete = async () => {
    if (!id) return
    if (!confirm('Delete this user?')) return
    try {
      await deleteUser(Number(id))
      navigate('/users')
    } catch (err) { console.error(err) }
  }

  if (loading) return <div className="form-root">Loading...</div>
  if (!user) return <div className="form-root">User not found</div>

  return (
    <div className="form-root">
      <h1>User #{user.id}</h1>
      {!editMode ? (
        <div>
          <div><strong>Email:</strong> {user.email}</div>
          <div><strong>Role:</strong> {user.role}</div>
          <div><strong>Created:</strong> {new Date(user.createdAt).toLocaleString()}</div>
          <div><strong>Updated:</strong> {new Date(user.updatedAt).toLocaleString()}</div>
          <div style={{ marginTop: 12 }}>
            <button onClick={() => setEditMode(true)}>Edit</button>
            <button onClick={onDelete} style={{ marginLeft: 8 }}>Delete</button>
            <button onClick={() => navigate('/users')} style={{ marginLeft: 8 }}>Back</button>
          </div>
        </div>
      ) : (
        <form onSubmit={onSave} className="form">
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
            <select value={role} onChange={(e) => setRole(e.target.value as any)}>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </label>
          <div className="form-actions">
            <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            <button type="button" onClick={() => setEditMode(false)}>Cancel</button>
          </div>
        </form>
      )}

      <section style={{ marginTop: 20 }}>
        <h2>Tickets ({userTickets.length})</h2>
        {ticketsLoading ? (
          <div>Loading tickets...</div>
        ) : userTickets.length === 0 ? (
          <div>No tickets assigned to this user</div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {userTickets.map((t) => (
              <div key={t.id} className="ticket-row" style={{ cursor: 'pointer' }} onClick={() => navigate(`/tickets/${t.id}`)}>
                <div className="ticket-title">{t.title}</div>
                <div className="ticket-meta">{t.priority} · {t.status}</div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  )
}
