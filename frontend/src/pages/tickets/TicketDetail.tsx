import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getTicket, updateTicket, deleteTicket } from '../../api/tickets_api'
import { getCompanies } from '../../api/company_api'
import { getCustomers } from '../../api/customers_api'
import { getUsers } from '../../api/users_api'
import type { TicketDTO, TicketUpdateDTO, Priority, Status } from '../../types/tickets'
import type { CompanyDTO } from '../../types/companies'
import type { CustomerDTO } from '../../types/customers'
import type { UserDTO } from '../../types/users'
import '../shared/form.css'

export default function TicketDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [ticket, setTicket] = useState<TicketDTO | null>(null)
  const [companies, setCompanies] = useState<CompanyDTO[]>([])
  const [customers, setCustomers] = useState<CustomerDTO[]>([])
  const [users, setUsers] = useState<UserDTO[]>([])
  const [editMode, setEditMode] = useState(false)
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [priority, setPriority] = useState<Priority>('LOW')
  const [status, setStatus] = useState<Status>('OPEN')
  const [companyId, setCompanyId] = useState<number | ''>('')
  const [customerId, setCustomerId] = useState<number | ''>('')
  const [assignedUserId, setAssignedUserId] = useState<number | ''>('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (!id) return
    Promise.all([getTicket(Number(id)), getCompanies(), getCustomers(), getUsers()])
      .then(([t, comps, custs, usrs]) => {
        setTicket(t)
        setTitle(t.title)
        setDescription(t.description)
        setPriority(t.priority)
        setStatus(t.status)
        setCompanyId(t.companyId ?? '')
        setCustomerId(t.customerId ?? '')
        setAssignedUserId(t.assignedUserId ?? '')
        setCompanies(comps ?? [])
        setCustomers(custs ?? [])
        setUsers(usrs ?? [])
      })
      .catch((err) => console.error(err))
      .finally(() => setLoading(false))
  }, [id])

  useEffect(() => setCustomerId(''), [companyId])

  const onSave = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!id) return
    setSaving(true)
    const payload: TicketUpdateDTO = {
      title,
      description,
      priority,
      status,
      companyId: Number(companyId),
      customerId: Number(customerId),
      assignedUserId: Number(assignedUserId),
    }
    try {
      const updated = await updateTicket(Number(id), payload)
      setTicket(updated)
      setEditMode(false)
    } catch (err) { console.error(err) } finally { setSaving(false) }
  }

  const onDelete = async () => {
    if (!id) return
    if (!confirm('Delete this ticket?')) return
    try {
      await deleteTicket(Number(id))
      navigate('/tickets')
    } catch (err) { console.error(err) }
  }

  const filteredCustomers = customers.filter((c) => companyId === '' ? true : c.companyId === Number(companyId))

  if (loading) return <div className="form-root">Loading...</div>
  if (!ticket) return <div className="form-root">Ticket not found</div>

  return (
    <div className="form-root">
      <h1>Ticket #{ticket.id}</h1>
      {!editMode ? (
        <div>
          <div><strong>Title:</strong> {ticket.title}</div>
          <div><strong>Description:</strong> {ticket.description}</div>
          <div><strong>Priority:</strong> {ticket.priority}</div>
          <div><strong>Status:</strong> {ticket.status}</div>
          <div><strong>Company:</strong> {ticket.companyId}</div>
          <div><strong>Customer:</strong> {ticket.customerId}</div>
          <div><strong>Assigned User:</strong> {ticket.assignedUserId}</div>
          <div style={{ marginTop: 12 }}>
            <button onClick={() => setEditMode(true)}>Edit</button>
            <button onClick={onDelete} style={{ marginLeft: 8 }}>Delete</button>
            <button onClick={() => navigate('/tickets')} style={{ marginLeft: 8 }}>Back</button>
          </div>
        </div>
      ) : (
        <form onSubmit={onSave} className="form">
          <label>
            Title
            <input value={title} onChange={(e) => setTitle(e.target.value)} required />
          </label>
          <label>
            Description
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} required />
          </label>
          <label>
            Priority
            <select value={priority} onChange={(e) => setPriority(e.target.value as Priority)}>
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
            </select>
          </label>
          <label>
            Status
            <select value={status} onChange={(e) => setStatus(e.target.value as Status)}>
              <option value="OPEN">OPEN</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="CLOSED">CLOSED</option>
            </select>
          </label>
          <label>
            Company
            <select value={companyId === '' ? '' : String(companyId)} onChange={(e) => setCompanyId(e.target.value ? Number(e.target.value) : '')}>
              <option value="">-- Select Company --</option>
              {companies.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </label>
          <label>
            Customer
            <select value={customerId === '' ? '' : String(customerId)} onChange={(e) => setCustomerId(e.target.value ? Number(e.target.value) : '')} disabled={companyId === ''}>
              <option value="">-- Select Customer --</option>
              {filteredCustomers.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </label>
          <label>
            Assigned User
            <select value={assignedUserId === '' ? '' : String(assignedUserId)} onChange={(e) => setAssignedUserId(e.target.value ? Number(e.target.value) : '')}>
              <option value="">-- Select User --</option>
              {users.map((u) => <option key={u.id} value={u.id}>{u.email}</option>)}
            </select>
          </label>
          <div className="form-actions">
            <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            <button type="button" onClick={() => setEditMode(false)}>Cancel</button>
          </div>
        </form>
      )}
    </div>
  )
}
