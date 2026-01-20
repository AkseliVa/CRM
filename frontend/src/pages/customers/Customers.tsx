import { useEffect, useState } from 'react'
import { getCustomers } from '../../api/customers_api'
import type { CustomerDTO } from '../../types/customers'
import './customers.css'
import { useNavigate } from 'react-router-dom'

export default function Customers() {
    const [customers, setCustomers] = useState<CustomerDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

    useEffect(() => {
        setLoading(true)
        getCustomers()
            .then((data) => setCustomers(data ?? []))
            .catch((err) => {
                console.error('Failed to load customers', err)
                setError('Failed to load customers')
            })
            .finally(() => setLoading(false))
    }, [])

    return (
        <div className="customers-root">
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h1>Customers</h1>
                <button onClick={() => navigate('/customers/new')}>Create Customer</button>
            </div>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : customers.length === 0 ? (
                <div>No customers found</div>
            ) : (
                <div className="customers-grid">
                    {customers.map((c) => (
                        <div className="customer-card" key={c.id}>
                            <div className="customer-name">{c.name}</div>
                            <div className="customer-company">Company: {c.companyId}</div>
                            <div className="customer-meta">{c.email} · {c.phone}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}