import petboxLogo from "../../../assets/smallLogo.svg"
import userIcon from "../../../assets/person-circle.svg"
import Sidebar from "./Sidebar.jsx";
import Cookies from "js-cookie";
import { useEffect, useState } from "react";

function Navbar() {
    const [collapsed, setCollapsed] = useState(false);
    const toggleSidebar = () => setCollapsed(!collapsed);

    function getUsernameFromCookie() {
        const session = Cookies.get("user_session")
        if (!session) return null

        try {
            const obj = JSON.parse(session)
            return obj.username
        } catch {
            return null
        }
    }


    return (
        <>
            <nav className="navbar navbar-dark sticky-top" style={{ backgroundColor: "#a1c8ff", fontWeight: "bold" }}>
                <button onClick={toggleSidebar} className="navbar-toggler mx-2" type="button">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <a className="navbar-brand" href="/profile">
                    <img src={userIcon} width="35" height="35" className="mx-3 d-inline-block align-top" alt="Foto do usuário"/>
                    {getUsernameFromCookie()}
                </a>

                <a className="navbar-brand ms-auto" href="/home">
                    PetBox
                    <img src={petboxLogo} width="35" height="35" className="mx-2 d-inline-block align-top" alt="Logo Petbox"
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