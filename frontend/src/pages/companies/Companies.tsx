import { useEffect } from "react"
import { fetchCompanies } from "../../api/company_api"

export default function Companies() {

    useEffect(() => {
        fetchCompanies();
    }, [])

    return (
        <div style={{ marginLeft: '250px', padding: '20px' }}>
            <h1>Companies</h1>
        </div>
    )
}