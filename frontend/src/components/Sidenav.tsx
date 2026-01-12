import { Sidebar, Menu, MenuItem } from 'react-pro-sidebar'
import { Link, useLocation } from 'react-router-dom'

export default function Sidenav() {
    const location = useLocation();

    return (
        <div style={{ height: '100%', position: 'absolute', left: 0, top: 0}}>
            <Sidebar
                rootStyles={{ height: "100%" }}
            >
                <Menu
                    menuItemStyles={{
                        button: ({ active }) => {
                        return {
                            backgroundColor: active ? '#3333ff' : undefined,
                            color: active ? '#b6c8d9' : undefined,
                            '&:hover': {
                            backgroundColor: '#99ccff'
                            },
                        };
                        },
                    }}
                    >
                    <MenuItem
                        active={location.pathname === '/'}
                        component={<Link to='/' />}
                    >
                        Dashboard
                    </MenuItem>

                    <MenuItem
                        active={location.pathname === '/companies'}
                        component={<Link to='/companies' />}
                    >
                        Companies
                    </MenuItem>

                    <MenuItem
                        active={location.pathname === '/customers'}
                        component={<Link to='/customers' />}
                    >
                        Customers
                    </MenuItem>

                    <MenuItem
                        active={location.pathname === '/tickets'}
                        component={<Link to='/tickets' />}
                    >
                        Tickets
                    </MenuItem>

                    <MenuItem
                        active={location.pathname === '/users'}
                        component={<Link to='/users' />}
                    >
                        Users
                    </MenuItem>
                </Menu>
            </Sidebar>
        </div>
    )
}