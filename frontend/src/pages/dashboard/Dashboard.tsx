import { useEffect, useState } from 'react'
import { getCompanies } from '../../api/company_api'
import { getCustomers } from '../../api/customers_api'
import { getUsers } from '../../api/users_api'
import { getTickets } from '../../api/tickets_api'
import type { CompanyDTO } from '../../types/companies'
import type { CustomerDTO } from '../../types/customers'
import type { UserDTO } from '../../types/users'
import type { TicketDTO } from '../../types/tickets'
import './dashboard.css'

export default function Dashboard() {
    const [companies, setCompanies] = useState<CompanyDTO[]>([])
    const [customers, setCustomers] = useState<CustomerDTO[]>([])
    const [users, setUsers] = useState<UserDTO[]>([])
    const [tickets, setTickets] = useState<TicketDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        setLoading(true)
        Promise.all([getCompanies(), getCustomers(), getUsers(), getTickets()])
            .then(([c, cu, u, t]) => {
                setCompanies(c ?? [])
                setCustomers(cu ?? [])
                setUsers(u ?? [])
                setTickets(t ?? [])
            })
            .catch((err) => {
                console.error('Dashboard load error', err)
                setError('Failed to load dashboard data')
            })
            .finally(() => setLoading(false))
    }, [])

    return (
        <div className="dashboard-root">
            <h1>Dashboard</h1>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : (
                <div className="dashboard-grid">
                    <section className="panel">
                        <h2>Companies</h2>
                        <ul>
                            {companies.map((c) => (
                                <li key={c.id}>{c.name}</li>
                            ))}
                        </ul>
                    </section>

                    <section className="panel">
                        <h2>Customers</h2>
                        <ul>
                            {customers.map((c) => (
                                <li key={c.id}>{c.name}</li>
                            ))}
                        </ul>
                    </section>

                    <section className="panel">
                        <h2>Users</h2>
                        <ul>
                            {users.map((u) => (
                                <li key={u.id}>{u.email}</li>
                            ))}
                        </ul>
                    </section>

                    <section className="panel">
                        <h2>Tickets</h2>
                        <ul>
                            {tickets.map((t) => (
                                <li key={t.id}>{t.title}</li>
                            ))}
                        </ul>
                    </section>
                </div>
            )}
        </div>
    )
}