import { useEffect, useState } from 'react'
import { getCompanies } from '../../api/company_api'
import type { CompanyDTO } from '../../types/companies'
import './companies.css'
import { useNavigate } from 'react-router-dom'

export default function Companies() {
    const [companies, setCompanies] = useState<CompanyDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        setLoading(true)
        getCompanies()
            .then((data) => setCompanies(data ?? []))
            .catch((err) => {
                console.error('Failed to load companies', err)
                setError('Failed to load companies')
            })
            .finally(() => setLoading(false))
    }, [])

    const navigate = useNavigate()

    return (
        <div className="companies-root">
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h1>Companies</h1>
                <button className="btn-primary" onClick={() => navigate('/companies/new')}>Create Company</button>
            </div>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : companies.length === 0 ? (
                <div>No companies found</div>
            ) : (
                <div className="companies-grid">
                    {companies.map((c) => (
                        <div
                            className="company-card"
                            key={c.id}
                            onClick={() => navigate(`/companies/${c.id}`)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div className="company-name">{c.name}</div>
                            <div className="company-industry">{c.industry}</div>
                            <div className="company-meta">Created: {new Date(c.createdAt).toLocaleString()}</div>
                            <div className="company-meta">Updated: {new Date(c.updatedAt).toLocaleString()}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}