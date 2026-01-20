import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createCompany } from '../../api/company_api'
import type { CompanyCreateDTO, Industry } from '../../types/companies'
import '../shared/form.css'

export default function CompanyCreate() {
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [industry, setIndustry] = useState<Industry>('FINANCE')
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setError(null)
    const payload: CompanyCreateDTO = { name, industry }
    try {
      await createCompany(payload)
      navigate('/companies')
    } catch (err) {
      console.error(err)
      setError('Failed to create company')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="form-root">
      <h1>Create Company</h1>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit} className="form">
        <label>
          Name
          <input value={name} onChange={(e) => setName(e.target.value)} required />
        </label>

        <label>
          Industry
          <select value={industry} onChange={(e) => setIndustry(e.target.value as Industry)}>
            <option value="FINANCE">FINANCE</option>
            <option value="MARKETING">MARKETING</option>
            <option value="IT">IT</option>
          </select>
        </label>

        <div className="form-actions">
          <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          <button type="button" onClick={() => navigate('/companies')}>Cancel</button>
        </div>
      </form>
    </div>
  )
}
