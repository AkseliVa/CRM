import api from './http'

export const getCompanies = async () => {
    const resp = await api.get('/companies')
    return resp.data
}

export const getCompany = async (id: number) => {
    const resp = await api.get(`/companies/${id}`)
    return resp.data
}

export const createCompany = async (payload: any) => {
    const resp = await api.post('/companies', payload)
    return resp.data
}

export const updateCompany = async (id: number, payload: any) => {
    const resp = await api.put(`/companies/${id}`, payload)
    return resp.data
}

export const deleteCompany = async (id: number) => {
    const resp = await api.delete(`/companies/${id}`)
    return resp.data
}

export default {
    getCompanies,
    getCompany,
    createCompany,
    updateCompany,
    deleteCompany,
}
