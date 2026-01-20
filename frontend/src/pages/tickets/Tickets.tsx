import { useEffect, useState } from 'react'
import { getTickets, updateTicket } from '../../api/tickets_api'
import type { TicketDTO } from '../../types/tickets'
import './tickets.css'
import { useNavigate } from 'react-router-dom'

export default function Tickets() {
    const [tickets, setTickets] = useState<TicketDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [updatingIds, setUpdatingIds] = useState<number[]>([])
    const navigate = useNavigate()

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

    const onChangePriority = async (ticket: TicketDTO, newPriority: TicketDTO['priority']) => {
        setUpdatingIds((s) => [...s, ticket.id])
        const payload = {
            title: ticket.title,
            description: ticket.description,
            priority: newPriority,
            status: ticket.status,
            companyId: ticket.companyId,
            customerId: ticket.customerId,
            assignedUserId: ticket.assignedUserId,
        }
        const prev = tickets
        setTickets((t) => t.map(x => x.id === ticket.id ? { ...x, priority: newPriority } : x))
        try {
            const updated = await updateTicket(ticket.id, payload)
            setTickets((t) => t.map(x => x.id === ticket.id ? updated : x))
        } catch (err) {
            console.error(err)
            setTickets(prev)
            setError('Failed to update priority')
        } finally {
            setUpdatingIds((s) => s.filter(i => i !== ticket.id))
        }
    }

    const onChangeStatus = async (ticket: TicketDTO, newStatus: TicketDTO['status']) => {
        setUpdatingIds((s) => [...s, ticket.id])
        const payload = {
            title: ticket.title,
            description: ticket.description,
            priority: ticket.priority,
            status: newStatus,
            companyId: ticket.companyId,
            customerId: ticket.customerId,
            assignedUserId: ticket.assignedUserId,
        }
        const prev = tickets
        setTickets((t) => t.map(x => x.id === ticket.id ? { ...x, status: newStatus } : x))
        try {
            const updated = await updateTicket(ticket.id, payload)
            setTickets((t) => t.map(x => x.id === ticket.id ? updated : x))
        } catch (err) {
            console.error(err)
            setTickets(prev)
            setError('Failed to update status')
        } finally {
            setUpdatingIds((s) => s.filter(i => i !== ticket.id))
        }
    }

    return (
        <div className="tickets-root">
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h1>Tickets</h1>
                <button className="btn-primary" onClick={() => navigate('/tickets/new')}>Create Ticket</button>
            </div>
            {error && <div className="error">{error}</div>}
            {loading ? (
                <div>Loading...</div>
            ) : tickets.length === 0 ? (
                <div>No tickets found</div>
            ) : (
                <div className="tickets-list">
                    {tickets.map((t) => (
                        <div
                            className="ticket-row"
                            key={t.id}
                        >
                            <div className="ticket-title" onClick={() => navigate(`/tickets/${t.id}`)} style={{ cursor: 'pointer' }}>{t.title}</div>
                            <div className="ticket-controls">
                                <select value={t.priority} onChange={(e) => onChangePriority(t, e.target.value as TicketDTO['priority'])} disabled={updatingIds.includes(t.id)}>
                                    <option value="LOW">LOW</option>
                                    <option value="MEDIUM">MEDIUM</option>
                                    <option value="HIGH">HIGH</option>
                                </select>
                                <select value={t.status} onChange={(e) => onChangeStatus(t, e.target.value as TicketDTO['status'])} disabled={updatingIds.includes(t.id)}>
                                    <option value="OPEN">OPEN</option>
                                    <option value="IN_PROGRESS">IN_PROGRESS</option>
                                    <option value="CLOSED">CLOSED</option>
                                </select>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}