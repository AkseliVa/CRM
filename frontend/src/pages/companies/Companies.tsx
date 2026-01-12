import { useEffect, useState } from "react"
import { fetchCompanies } from "../../api/company_api"
import type { CompanyDTO } from "./companyTypes";

export default function Companies() {
    const [companies, setCompanies] = useState([]);

    useEffect(() => {
        async function getCompanies() {
            const data = await fetchCompanies();
            setCompanies(data);
        }
        getCompanies();   
    }, []);

    const companyObjects = companies.map((company: CompanyDTO) => <li key={company.id}>{company.name}</li>)

    return (
        <div style={{ marginLeft: '250px', padding: '20px' }}>
            <h1>Companies</h1>
            <ul>{companyObjects}</ul>
        </div>
    )
}