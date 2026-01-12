import { useEffect } from "react"
import { fetchCompanies } from "../../api/company_api";

export default function Dashboard() {

    useEffect(() => {
        fetchCompanies();
    }, []);

    return (
        <>
            <h1>Dashboard</h1>
        </>
    )
}