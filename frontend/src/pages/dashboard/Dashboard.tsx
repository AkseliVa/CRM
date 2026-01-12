import { useEffect } from "react"
import { fetchCompanies } from "../../api/company_api";

export default function Dashboard() {

    useEffect(() => {
        fetchCompanies();
    }, []);

    return (
        <div style={{ marginLeft: '250px', padding: '20px' }}>
            <h1>Dashboard</h1>
        </div>
    )
}