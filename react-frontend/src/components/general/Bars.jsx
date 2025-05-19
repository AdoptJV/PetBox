import { useState } from "react";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";

function Bars({ children }) {
    const [collapsed, setCollapsed] = useState(false);
    const toggleSidebar = () => setCollapsed(!collapsed);

    return (
        <div style={{ backgroundColor: "#f3f3f3", minHeight: "100vh" }}>
            <Navbar collapsed={collapsed} toggleSidebar={toggleSidebar} />
            <Sidebar collapsed={collapsed} />
            <div style={{
                marginLeft: collapsed ? "0" : "250px",
                transition: "margin-left 0.3s ease"
            }}>
                {children}
            </div>
        </div>
    );
}

export default Bars