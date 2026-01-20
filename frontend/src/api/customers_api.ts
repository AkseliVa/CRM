import api from './http'

export const getCustomers = async () => {
  const resp = await api.get('/customers')
  return resp.data
}

export const getCustomer = async (id: number) => {
  const resp = await api.get(`/customers/${id}`)
  return resp.data
}

export const createCustomer = async (payload: any) => {
  const resp = await api.post('/customers', payload)
  return resp.data
}

export const updateCustomer = async (id: number, payload: any) => {
  const resp = await api.put(`/customers/${id}`, payload)
  return resp.data
}

export const deleteCustomer = async (id: number) => {
  const resp = await api.delete(`/customers/${id}`)
  return resp.data
}

export default { getCustomers, getCustomer, createCustomer, updateCustomer, deleteCustomer }
