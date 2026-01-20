import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getCustomer, updateCustomer, deleteCustomer } from '../../api/customers_api'
import { getCompany } from '../../api/company_api'
import { getCustomerNotes, createCustomerNote, updateCustomerNote, deleteCustomerNote } from '../../api/customer_notes_api'
import { getUsers } from '../../api/users_api'
import type { CustomerDTO, CustomerUpdateDTO } from '../../types/customers'
import type { CompanyDTO } from '../../types/companies'
import type { CustomerNoteDTO, CustomerNoteCreateDTO, CustomerNoteUpdateDTO } from '../../types/customerNotes'
import type { UserDTO } from '../../types/users'
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
  const [notes, setNotes] = useState<CustomerNoteDTO[]>([])
  const [notesLoading, setNotesLoading] = useState(false)
  const [newNoteContent, setNewNoteContent] = useState('')
  const [newNoteUserId, setNewNoteUserId] = useState<number | ''>('')
  const [adding, setAdding] = useState(false)
  const [editingNoteId, setEditingNoteId] = useState<number | null>(null)
  const [editContent, setEditContent] = useState('')
  const [users, setUsers] = useState<UserDTO[]>([])

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

  useEffect(() => {
    if (!id) return
    setNotesLoading(true)
    getCustomerNotes()
      .then((all) => {
        const filtered = (all || []).filter((n: CustomerNoteDTO) => String(n.customerId) === String(id))
        setNotes(filtered.sort((a: any, b: any) => Date.parse(b.updatedAt) - Date.parse(a.updatedAt)))
      })
      .catch(() => {})
      .finally(() => setNotesLoading(false))
  }, [id])

  useEffect(() => {
    getUsers().then((u) => setUsers(u ?? [])).catch(() => {})
  }, [])

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

  const onAddNote = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!id || !newNoteContent) return
    const userId = typeof newNoteUserId === 'number' && newNoteUserId > 0 ? newNoteUserId : undefined
    const payload: CustomerNoteCreateDTO = { customerId: Number(id), userId: userId ?? 0, content: newNoteContent }
    setAdding(true)
    try {
      const created = await createCustomerNote(payload)
      setNotes((prev) => [created, ...prev])
      setNewNoteContent('')
      setNewNoteUserId('')
    } catch (err) { console.error(err) } finally { setAdding(false) }
  }

  const onStartEdit = (note: CustomerNoteDTO) => {
    setEditingNoteId(note.id)
    setEditContent(note.content)
  }

  const onSaveEdit = async (noteId: number) => {
    try {
      const payload: CustomerNoteUpdateDTO = { customerId: Number(id), userId: notes.find(n => n.id === noteId)?.userId ?? 0, content: editContent }
      const updated = await updateCustomerNote(noteId, payload)
      setNotes((prev) => prev.map(n => n.id === noteId ? updated : n))
      setEditingNoteId(null)
      setEditContent('')
    } catch (err) { console.error(err) }
  }

  const onDeleteNote = async (noteId: number) => {
    if (!confirm('Delete this note?')) return
    try {
      await deleteCustomerNote(noteId)
      setNotes((prev) => prev.filter(n => n.id !== noteId))
    } catch (err) { console.error(err) }
  }

  if (loading) return <div className="form-root">Loading...</div>
  if (!customer) return <div className="form-root">Customer not found</div>

  return (
    <div className="form-root">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
        <h1>Customer #{customer.id}</h1>
        <div className="page-actions">
          <button onClick={() => navigate('/customers')}>Back</button>
          <button onClick={() => setEditMode(true)}>Edit</button>
          <button onClick={onDelete}>Delete</button>
        </div>
      </div>
      {!editMode ? (
        <div>
          <div><strong>Name:</strong> {customer.name}</div>
          <div><strong>Email:</strong> {customer.email}</div>
          <div><strong>Phone:</strong> {customer.phone}</div>
          <div><strong>Company:</strong> {company ? company.name : customer.companyId ?? '—'}</div>
            <div style={{ marginTop: 12 }}>
              <h3>Notes</h3>
              {notesLoading ? <div>Loading notes...</div> : (
                <div>
                  <form onSubmit={onAddNote} className="form" style={{ marginBottom: 12 }}>
                    <label>
                      Content
                      <textarea value={newNoteContent} onChange={(e) => setNewNoteContent(e.target.value)} required />
                    </label>
                    <label>
                      User
                      <select value={newNoteUserId as any} onChange={(e) => setNewNoteUserId(e.target.value ? Number(e.target.value) : '')}>
                        <option value="">Select user</option>
                        {users.map(u => (
                          <option key={u.id} value={u.id}>{u.email}</option>
                        ))}
                      </select>
                    </label>
                    <div className="form-actions">
                      <button type="submit" disabled={adding}>{adding ? 'Adding...' : 'Add Note'}</button>
                    </div>
                  </form>

                  {notes.length === 0 ? <div className="empty">No notes</div> : (
                    <div className="notes-list">
                      {notes.map((n) => (
                        <div key={n.id} style={{ borderBottom: '1px solid rgba(0,0,0,0.06)', padding: 8 }}>
                          <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
                            <div style={{ fontWeight: 600 }}>By: {users.find(u => u.id === n.userId)?.email ?? n.userId}</div>
                            <div style={{ color: 'var(--muted)' }}>{new Date(n.updatedAt).toLocaleString()}</div>
                          </div>
                          {editingNoteId === n.id ? (
                            <div>
                              <textarea value={editContent} onChange={(e) => setEditContent(e.target.value)} />
                              <div style={{ marginTop: 6 }}>
                                <button onClick={() => onSaveEdit(n.id)}>Save</button>
                                <button onClick={() => { setEditingNoteId(null); setEditContent('') }} style={{ marginLeft: 8 }}>Cancel</button>
                              </div>
                            </div>
                          ) : (
                            <div>
                              <div style={{ marginTop: 8 }}>{n.content}</div>
                              <div style={{ marginTop: 8 }}>
                                <button onClick={() => onStartEdit(n)}>Edit</button>
                                <button onClick={() => onDeleteNote(n.id)} style={{ marginLeft: 8 }}>Delete</button>
                              </div>
                            </div>
                          )}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
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
