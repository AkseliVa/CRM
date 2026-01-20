import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import Dashboard from "./pages/dashboard/Dashboard.tsx"
import Companies from "./pages/companies/Companies.tsx"
import CompanyCreate from "./pages/companies/CompanyCreate.tsx"
import CompanyDetail from "./pages/companies/CompanyDetail.tsx"
import Customers from "./pages/customers/Customers.tsx"
import CustomerCreate from "./pages/customers/CustomerCreate.tsx"
import CustomerDetail from "./pages/customers/CustomerDetail.tsx"
import Tickets from "./pages/tickets/Tickets.tsx"
import TicketCreate from "./pages/tickets/TicketCreate.tsx"
import TicketDetail from "./pages/tickets/TicketDetail.tsx"
import Users from "./pages/users/Users.tsx"
import UserCreate from "./pages/users/UserCreate.tsx"
import UserDetail from "./pages/users/UserDetail.tsx"
import Sidenav from "./components/Sidenav.tsx"

const root = document.getElementById('root');
if (!root) throw new Error("No root element found");

ReactDOM.createRoot(root).render(
  <BrowserRouter>
  <Sidenav />
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/companies" element={<Companies />} />
      <Route path="/companies/new" element={<CompanyCreate />} />
      <Route path="/companies/:id" element={<CompanyDetail />} />
      <Route path="/customers" element={<Customers />} />
      <Route path="/customers/new" element={<CustomerCreate />} />
      <Route path="/customers/:id" element={<CustomerDetail />} />
      <Route path="/tickets" element={<Tickets />} />
      <Route path="/tickets/new" element={<TicketCreate />} />
      <Route path="/tickets/:id" element={<TicketDetail />} />
      <Route path="/users" element={<Users />} />
      <Route path="/users/new" element={<UserCreate />} />
      <Route path="/users/:id" element={<UserDetail />} />
    </Routes>
  </BrowserRouter>
)
