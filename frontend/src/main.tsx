import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import './index.css'
import Dashboard from "./pages/dashboard/Dashboard.tsx"
import Companies from "./pages/companies/Companies.tsx"
import Customers from "./pages/customers/Customers.tsx"
import Tickets from "./pages/tickets/Tickets.tsx"
import Users from "./pages/users/Users.tsx"
import Sidenav from "./components/Sidenav.tsx"

const root = document.getElementById('root');
if (!root) throw new Error("No root element found");

ReactDOM.createRoot(root).render(
  <BrowserRouter>
  <Sidenav />
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/companies" element={<Companies />} />
      <Route path="/customers" element={<Customers />} />
      <Route path="/tickets" element={<Tickets />} />
      <Route path="/users" element={<Users />} />
    </Routes>
  </BrowserRouter>
)
