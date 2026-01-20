import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createTicket } from '../../api/tickets_api'
import { getCompanies } from '../../api/company_api'
import { getCustomers } from '../../api/customers_api'
import { getUsers } from '../../api/users_api'
import type { TicketCreateDTO, Priority, Status } from '../../types/tickets'
import type { CompanyDTO } from '../../types/companies'
import type { CustomerDTO } from '../../types/customers'
import type { UserDTO } from '../../types/users'
import '../shared/form.css'

export default function TicketCreate() {
  const navigate = useNavigate()
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [priority, setPriority] = useState<Priority>('LOW')
  const [status, setStatus] = useState<Status>('OPEN')

  const [companies, setCompanies] = useState<CompanyDTO[]>([])
  const [customers, setCustomers] = useState<CustomerDTO[]>([])
  const [users, setUsers] = useState<UserDTO[]>([])

  const [companyId, setCompanyId] = useState<number | ''>('')
  const [customerId, setCustomerId] = useState<number | ''>('')
  const [assignedUserId, setAssignedUserId] = useState<number | ''>('')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    // load companies, customers, users
    Promise.all([getCompanies(), getCustomers(), getUsers()])
      .then(([c, cu, u]) => {
        setCompanies(c ?? [])
        setCustomers(cu ?? [])
        setUsers(u ?? [])
      })
      .catch((err) => console.error('Failed to load select data', err))
  }, [])

  // when company changes, reset selected customer
  useEffect(() => {
    setCustomerId('')
  }, [companyId])

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setError(null)
    const payload: TicketCreateDTO = {
      title,
      description,
      priority,
      status,
      companyId: Number(companyId),
      customerId: Number(customerId),
      assignedUserId: Number(assignedUserId),
    }
    try {
      await createTicket(payload)
      navigate('/tickets')
    } catch (err) {
      console.error(err)
      setError('Failed to create ticket')
    } finally {
      setSaving(false)
    }
  }

  const filteredCustomers = customers.filter((c) => companyId === '' ? true : c.companyId === Number(companyId))

  return (
    <div className="form-root">
      <h1>Create Ticket</h1>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit} className="form">
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
          <select
            value={companyId === '' ? '' : String(companyId)}
            onChange={(e) => setCompanyId(e.target.value ? Number(e.target.value) : '')}
            required
          >
            <option value="">-- Select Company --</option>
            {companies.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </label>

        <label>
          Customer
          <select
            value={customerId === '' ? '' : String(customerId)}
            onChange={(e) => setCustomerId(e.target.value ? Number(e.target.value) : '')}
            required
            disabled={companyId === ''}
          >
            <option value="">-- Select Customer --</option>
            {filteredCustomers.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </label>

        <label>
          Assigned User
          <select
            value={assignedUserId === '' ? '' : String(assignedUserId)}
            onChange={(e) => setAssignedUserId(e.target.value ? Number(e.target.value) : '')}
            required
          >
            <option value="">-- Select User --</option>
            {users.map((u) => (
              <option key={u.id} value={u.id}>{u.email}</option>
            ))}
          </select>
        </label>

        <div className="form-actions">
          <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          <button type="button" onClick={() => navigate('/tickets')}>Cancel</button>
        </div>
      </form>
    </div>
  )
}
