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
import { useNavigate } from 'react-router-dom'

export default function Dashboard() {
    const [companies, setCompanies] = useState<CompanyDTO[]>([])
    const [customers, setCustomers] = useState<CustomerDTO[]>([])
    const [users, setUsers] = useState<UserDTO[]>([])
    const [tickets, setTickets] = useState<TicketDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate()

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
                        <div className="panel-body">
                            <div className="all-list">
                                <strong>All companies</strong>
                                <ul>
                                    {companies.map((c) => (
                                        <li key={c.id} className="list-item" onClick={() => navigate(`/companies/${c.id}`)}>{c.name}</li>
                                    ))}
                                </ul>
                            </div>

                            <div className="updated-latest">
                                <strong>Updated latest</strong>
                                {companies.length === 0 ? (
                                    <div className="empty">No companies</div>
                                ) : (
                                    [...companies]
                                        .sort((a, b) => Date.parse(b.updatedAt) - Date.parse(a.updatedAt))
                                        .map((last) => (
                                            <div key={last.id} className="item" onClick={() => navigate(`/companies/${last.id}`)}>
                                                <div className="item-title">{last.name}</div>
                                                <div className="item-meta">Updated: {new Date(last.updatedAt).toLocaleString()}</div>
                                            </div>
                                        ))
                                )}
                            </div>
                        </div>
                    </section>

                    <section className="panel">
                        <h2>Customers</h2>
                        <div className="panel-body">
                            <div className="all-list">
                                <strong>All customers</strong>
                                <ul>
                                    {customers.map((c) => (
                                        <li key={c.id} className="list-item" onClick={() => navigate(`/customers/${c.id}`)}>{c.name}</li>
                                    ))}
                                </ul>
                            </div>

                            <div className="updated-latest">
                                <strong>Updated latest</strong>
                                {customers.length === 0 ? (
                                    <div className="empty">No customers</div>
                                ) : (
                                    [...customers]
                                        .sort((a, b) => Date.parse(b.updatedAt) - Date.parse(a.updatedAt))
                                        .map((last) => (
                                            <div key={last.id} className="item" onClick={() => navigate(`/customers/${last.id}`)}>
                                                <div className="item-title">{last.name}</div>
                                                <div className="item-meta">Updated: {new Date(last.updatedAt).toLocaleString()}</div>
                                            </div>
                                        ))
                                )}
                            </div>
                        </div>
                    </section>

                    <section className="panel">
                        <h2>Users</h2>
                        <div className="panel-body">
                            <div className="all-list">
                                <strong>All users</strong>
                                <ul>
                                    {users.map((u) => (
                                        <li key={u.id} className="list-item" onClick={() => navigate(`/users/${u.id}`)}>{u.email}</li>
                                    ))}
                                </ul>
                            </div>

                            <div className="updated-latest">
                                <strong>Updated latest</strong>
                                {users.length === 0 ? (
                                    <div className="empty">No users</div>
                                ) : (
                                    [...users]
                                        .sort((a, b) => Date.parse(b.updatedAt) - Date.parse(a.updatedAt))
                                        .map((last) => (
                                            <div key={last.id} className="item" onClick={() => navigate(`/users/${last.id}`)}>
                                                <div className="item-title">{last.email}</div>
                                                <div className="item-meta">Updated: {new Date(last.updatedAt).toLocaleString()}</div>
                                            </div>
                                        ))
                                )}
                            </div>
                        </div>
                    </section>

                    <section className="panel">
                        <h2>Tickets</h2>
                        <div className="panel-body">
                            <div className="all-list">
                                <strong>All tickets</strong>
                                <ul>
                                    {tickets.map((t) => (
                                        <li key={t.id} className="list-item" onClick={() => navigate(`/tickets/${t.id}`)}>
                                            {t.title} <span className={`status status-${t.status.toLowerCase()}`}>{t.status}</span>
                                        </li>
                                    ))}
                                </ul>
                            </div>

                            <div className="updated-latest">
                                <strong>Updated latest</strong>
                                {tickets.length === 0 ? (
                                    <div className="empty">No tickets</div>
                                ) : (
                                    [...tickets]
                                        .sort((a, b) => Date.parse(b.updatedAt) - Date.parse(a.updatedAt))
                                        .map((last) => (
                                            <div key={last.id} className="item" onClick={() => navigate(`/tickets/${last.id}`)}>
                                                <div className="item-title">{last.title} <span className={`status status-${last.status.toLowerCase()}`}>{last.status}</span></div>
                                                <div className="item-meta">Updated: {new Date(last.updatedAt).toLocaleString()}</div>
                                            </div>
                                        ))
                                )}
                            </div>
                        </div>
                    </section>
                </div>
            )}
        </div>
    )
}