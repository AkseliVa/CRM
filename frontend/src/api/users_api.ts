import api from './http'

export const getUsers = async () => {
  const resp = await api.get('/users')
  return resp.data
}

export const getUser = async (id: number) => {
  const resp = await api.get(`/users/${id}`)
  return resp.data
}

export const createUser = async (payload: any) => {
  const resp = await api.post('/users', payload)
  return resp.data
}

export const updateUser = async (id: number, payload: any) => {
  const resp = await api.put(`/users/${id}`, payload)
  return resp.data
}

export const deleteUser = async (id: number) => {
  const resp = await api.delete(`/users/${id}`)
  return resp.data
}

export default { getUsers, getUser, createUser, updateUser, deleteUser }
