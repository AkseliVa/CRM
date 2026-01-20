import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createCustomer } from '../../api/customers_api'
import type { CustomerCreateDTO } from '../../types/customers'
import '../shared/form.css'
import type { CompanyDTO } from '../../types'
import { getCompanies } from '../../api/company_api'

export default function CustomerCreate() {
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [phone, setPhone] = useState('')
  const [companyId, setCompanyId] = useState<number | ''>('')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const [companies, setCompanies] = useState<CompanyDTO[]>([])

  useEffect(() => {
      Promise.all([getCompanies()])
        .then(([c]) => {
          setCompanies(c ?? [])
        })
        .catch((err) => console.error('Failed to load select data', err))
    }, [])

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setError(null)
    const payload: CustomerCreateDTO = { name, email, phone, companyId: Number(companyId) }
    try {
      await createCustomer(payload)
      navigate('/customers')
    } catch (err) {
      console.error(err)
      setError('Failed to create customer')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="form-root">
      <h1>Create Customer</h1>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit} className="form">
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

        <div className="form-actions">
          <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          <button type="button" onClick={() => navigate('/customers')}>Cancel</button>
        </div>
      </form>
    </div>
  )
}
