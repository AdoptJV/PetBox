import petboxLogo from "../../../assets/smallLogo.svg"
import Sidebar from "./Sidebar.jsx";
import { useEffect, useState } from "react";

function Navbar() {
    const [collapsed, setCollapsed] = useState(false);
    const toggleSidebar = () => setCollapsed(!collapsed);

    return (
        <>
            <nav className="navbar navbar-dark" style={{ backgroundColor: "#a1c8ff" }}>
                <button onClick={toggleSidebar} className="navbar-toggler mx-2" type="button">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <a className="navbar-brand ms-auto" href="#">
                    PetBox
                    <img src={petboxLogo} width="35" height="35" className="mx-2 d-inline-block align-top" alt=""
                         style={{ filter: "drop-shadow(0 0px 1px rgba(0,0,0,0.2))" +
                                 "drop-shadow(0 1px 2px rgba(0,0,0,0.2)) " +
                                 "drop-shadow(0 2px 4px rgba(0,0,0,0.2))",
                             transform: "translateZ(0)" }}
                    />
                </a>
            </nav>
            <Sidebar collapsed={collapsed} toggleSidebar={toggleSidebar} />
            </>
    );
}

export default Navbar;