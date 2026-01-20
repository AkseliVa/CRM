import { useEffect, useState } from 'react'
import { getTickets } from '../../api/tickets_api'
import type { TicketDTO } from '../../types/tickets'
import './tickets.css'

export default function Tickets() {
    const [tickets, setTickets] = useState<TicketDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        setLoading(true)
        getTickets()
            .then((data) => setTickets(data ?? []))
            .catch((err) => {
                console.error('Failed to load tickets', err)
                setError('Failed to load tickets')
            })
            .finally(() => setLoading(false))
    }, [])

    return (
        <div className="tickets-root">
            <h1>Tickets</h1>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : tickets.length === 0 ? (
                <div>No tickets found</div>
            ) : (
                <div className="tickets-list">
                    {tickets.map((t) => (
                        <div className="ticket-row" key={t.id}>
                            <div className="ticket-title">{t.title}</div>
                            <div className="ticket-meta">{t.priority} · {t.status}</div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}