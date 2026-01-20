import api from './http'

export const getTickets = async () => {
  const resp = await api.get('/tickets')
  return resp.data
}

export const getTicket = async (id: number) => {
  const resp = await api.get(`/tickets/${id}`)
  return resp.data
}

export const createTicket = async (payload: any) => {
  const resp = await api.post('/tickets', payload)
  return resp.data
}

export const updateTicket = async (id: number, payload: any) => {
  const resp = await api.put(`/tickets/${id}`, payload)
  return resp.data
}

export const deleteTicket = async (id: number) => {
  const resp = await api.delete(`/tickets/${id}`)
  return resp.data
}

export default { getTickets, getTicket, createTicket, updateTicket, deleteTicket }
