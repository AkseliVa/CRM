import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getCompany, updateCompany, deleteCompany } from '../../api/company_api'
import { getCustomers } from '../../api/customers_api'
import { getTickets } from '../../api/tickets_api'
import type { CompanyDTO, CompanyUpdateDTO } from '../../types/companies'
import type { CustomerDTO } from '../../types/customers'
import type { TicketDTO } from '../../types/tickets'
import '../shared/form.css'

export default function CompanyDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [company, setCompany] = useState<CompanyDTO | null>(null)
  const [editMode, setEditMode] = useState(false)
  const [name, setName] = useState('')
  const [industry, setIndustry] = useState<CompanyDTO['industry']>('FINANCE')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [customers, setCustomers] = useState<CustomerDTO[]>([])
  const [tickets, setTickets] = useState<TicketDTO[]>([])

  useEffect(() => {
    if (!id) return
    setLoading(true)
    Promise.all([getCompany(Number(id)), getCustomers(), getTickets()])
      .then(([c, custs, tics]) => {
        setCompany(c)
        setName(c.name)
        setIndustry(c.industry)
        setCustomers(custs ?? [])
        setTickets(tics ?? [])
      })
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [id])

  const onSave = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!id) return
    setSaving(true)
    const payload: CompanyUpdateDTO = { name, industry }
    try {
      const updated = await updateCompany(Number(id), payload)
      setCompany(updated)
      setEditMode(false)
    } catch (err) {
      console.error(err)
    } finally { setSaving(false) }
  }

  const onDelete = async () => {
    if (!id) return
    if (!confirm('Delete this company?')) return
    try {
      await deleteCompany(Number(id))
      navigate('/companies')
    } catch (err) { console.error(err) }
  }

  if (loading) return <div className="form-root">Loading...</div>
  if (!company) return <div className="form-root">Company not found</div>

  return (
    <div className="form-root">
      <h1>Company #{company.id}</h1>
      {!editMode ? (
        <div>
          <div><strong>Name:</strong> {company.name}</div>
          <div><strong>Industry:</strong> {company.industry}</div>
          <div><strong>Created:</strong> {new Date(company.createdAt).toLocaleString()}</div>
          <div><strong>Updated:</strong> {new Date(company.updatedAt).toLocaleString()}</div>
          <div style={{ marginTop: 12 }}>
            <button onClick={() => setEditMode(true)}>Edit</button>
            <button onClick={onDelete} style={{ marginLeft: 8 }}>Delete</button>
            <button onClick={() => navigate('/companies')} style={{ marginLeft: 8 }}>Back</button>
          </div>
        </div>
      ) : (
        <form onSubmit={onSave} className="form">
          <label>
            Name
            <input value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            Industry
            <select value={industry} onChange={(e) => setIndustry(e.target.value as any)}>
              <option value="FINANCE">FINANCE</option>
              <option value="MARKETING">MARKETING</option>
              <option value="IT">IT</option>
            </select>
          </label>
          <div className="form-actions">
            <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            <button type="button" onClick={() => setEditMode(false)}>Cancel</button>
          </div>
        </form>
      )}

      <section style={{ marginTop: 20 }}>
        <h2>Customers</h2>
        {customers.filter((c) => c.companyId === company.id).length === 0 ? (
          <div>No customers for this company</div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: 12 }}>
            {customers.filter((c) => c.companyId === company.id).map((c) => (
              <div key={c.id} className="customer-card" style={{ cursor: 'pointer' }} onClick={() => navigate(`/customers/${c.id}`)}>
                <div className="customer-name">{c.name}</div>
                <div className="customer-meta">{c.email} · {c.phone}</div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section style={{ marginTop: 20 }}>
        <h2>Tickets</h2>
        {tickets.filter((t) => t.companyId === company.id).length === 0 ? (
          <div>No tickets for this company</div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {tickets.filter((t) => t.companyId === company.id).map((t) => (
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
