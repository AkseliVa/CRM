import axios from 'axios'

const API_BASE_URL: string = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
    baseURL: API_BASE_URL
});

export async function fetchCompanies() {
    try {
        const response = await api.get('/companies')
        console.log('Companies data:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching companies:', error);
        throw error;
    }
};
