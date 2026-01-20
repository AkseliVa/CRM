import { useEffect, useState } from 'react'
import { getCustomers } from '../../api/customers_api'
import { getCompanies } from '../../api/company_api'
import type { CustomerDTO } from '../../types/customers'
import type { CompanyDTO } from '../../types/companies'
import './customers.css'
import { useNavigate } from 'react-router-dom'

export default function Customers() {
    const [customers, setCustomers] = useState<CustomerDTO[]>([])
    const [companies, setCompanies] = useState<CompanyDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

    useEffect(() => {
        setLoading(true)
        Promise.all([getCustomers(), getCompanies()])
            .then(([cust, comps]) => {
                setCustomers(cust ?? [])
                setCompanies(comps ?? [])
            })
            .catch((err) => {
                console.error('Failed to load customers or companies', err)
                setError('Failed to load data')
            })
            .finally(() => setLoading(false))
    }, [])

    const grouped = companies.map((company) => ({
        company,
        customers: customers.filter((c) => c.companyId === company.id),
    }))

    const unassigned = customers.filter((c) => !companies.find((co) => co.id === c.companyId))

    return (
        <div className="customers-root">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h1>Customers</h1>
                <button className="btn-primary" onClick={() => navigate('/customers/new')}>Create Customer</button>
            </div>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : customers.length === 0 ? (
                <div>No customers found</div>
            ) : (
                <div className="customers-by-company">
                    {grouped.map(({ company, customers: group }) => (
                        <div key={company.id} className="company-group">
                            <h2>{company.name}</h2>
                            {group.length === 0 ? (
                                <div className="no-items">No customers for this company</div>
                            ) : (
                                <div className="customers-grid">
                                    {group.map((c) => (
                                        <div
                                            className="customer-card"
                                            key={c.id}
                                            onClick={() => navigate(`/customers/${c.id}`)}
                                            style={{ cursor: 'pointer' }}
                                        >
                                            <div className="customer-name">{c.name}</div>
                                            <div className="customer-meta">{c.email} · {c.phone}</div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}

                    {unassigned.length > 0 && (
                        <div className="company-group">
                            <h2>Unassigned</h2>
                            <div className="customers-grid">
                                {unassigned.map((c) => (
                                    <div className="customer-card" key={c.id}>
                                        <div className="customer-name">{c.name}</div>
                                        <div className="customer-meta">{c.email} · {c.phone}</div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    )
}