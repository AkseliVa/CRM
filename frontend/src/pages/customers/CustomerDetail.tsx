import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getCustomer, updateCustomer, deleteCustomer } from '../../api/customers_api'
import { getCompany } from '../../api/company_api'
import type { CustomerDTO, CustomerUpdateDTO } from '../../types/customers'
import type { CompanyDTO } from '../../types/companies'
import '../shared/form.css'

export default function CustomerDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [customer, setCustomer] = useState<CustomerDTO | null>(null)
  const [company, setCompany] = useState<CompanyDTO | null>(null)
  const [editMode, setEditMode] = useState(false)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [phone, setPhone] = useState('')
  const [companyId, setCompanyId] = useState<number | null>(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (!id) return
    getCustomer(Number(id)).then((c) => {
      setCustomer(c)
      setName(c.name)
      setEmail(c.email)
      setPhone(c.phone)
      setCompanyId(c.companyId ?? null)
      if (c.companyId) getCompany(c.companyId).then(setCompany).catch(()=>{})
      setLoading(false)
    }).catch(()=>setLoading(false))
  }, [id])

  const onSave = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!id) return
    setSaving(true)
    const payload: CustomerUpdateDTO = { name, email, phone, companyId: companyId ?? undefined }
    try {
      const updated = await updateCustomer(Number(id), payload)
      setCustomer(updated)
      setEditMode(false)
    } catch (err) { console.error(err) } finally { setSaving(false) }
  }

  const onDelete = async () => {
    if (!id) return
    if (!confirm('Delete this customer?')) return
    try {
      await deleteCustomer(Number(id))
      navigate('/customers')
    } catch (err) { console.error(err) }
  }

  if (loading) return <div className="form-root">Loading...</div>
  if (!customer) return <div className="form-root">Customer not found</div>

  return (
    <div className="form-root">
      <h1>Customer #{customer.id}</h1>
      {!editMode ? (
        <div>
          <div><strong>Name:</strong> {customer.name}</div>
          <div><strong>Email:</strong> {customer.email}</div>
          <div><strong>Phone:</strong> {customer.phone}</div>
          <div><strong>Company:</strong> {company ? company.name : customer.companyId ?? '—'}</div>
          <div style={{ marginTop: 12 }}>
            <button onClick={() => setEditMode(true)}>Edit</button>
            <button onClick={onDelete} style={{ marginLeft: 8 }}>Delete</button>
            <button onClick={() => navigate('/customers')} style={{ marginLeft: 8 }}>Back</button>
          </div>
        </div>
      ) : (
        <form onSubmit={onSave} className="form">
          <label>
            Name
            <input value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            Email
            <input value={email} onChange={(e) => setEmail(e.target.value)} required type="email" />
          </label>
          <label>
            Phone
            <input value={phone} onChange={(e) => setPhone(e.target.value)} required />
          </label>
          <label>
            Company ID
            <input value={companyId ?? '' as any} onChange={(e) => setCompanyId(e.target.value ? Number(e.target.value) : null)} />
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
