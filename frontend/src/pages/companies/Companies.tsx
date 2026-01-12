import { useEffect } from "react"
import { fetchCompanies } from "../../api/company_api"

export default function Companies() {

    useEffect(() => {
        fetchCompanies();
    }, [])

    return (
        <>
            <h1>Companies</h1>
        </>
    )
}