import api from './http'

export const getCustomerNotes = async () => {
  const resp = await api.get('/customer-notes')
  return resp.data
}

export const getCustomerNote = async (id: number) => {
  const resp = await api.get(`/customer-notes/${id}`)
  return resp.data
}

export const createCustomerNote = async (payload: any) => {
  const resp = await api.post('/customer-notes', payload)
  return resp.data
}

export const updateCustomerNote = async (id: number, payload: any) => {
  const resp = await api.put(`/customer-notes/${id}`, payload)
  return resp.data
}

export const deleteCustomerNote = async (id: number) => {
  const resp = await api.delete(`/customer-notes/${id}`)
  return resp.data
}

export default { getCustomerNotes, getCustomerNote, createCustomerNote, updateCustomerNote, deleteCustomerNote }
