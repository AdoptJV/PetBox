import petboxLogo from "../../../assets/smallLogo.svg"
import userIcon from "../../../assets/person-circle.svg"
import { useEffect, useState } from "react";

function Navbar({ collapsed, toggleSidebar }) {
    const [username, setUsername] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/home", {
            credentials: "include",
        })
            .then(res => res.json())
            .then(data => setUsername(data.username))
            .catch(() => setUsername(""));
    }, []);

    return (
        <nav className="navbar navbar-dark sticky-top" style={{ backgroundColor: "#a1c8ff", fontWeight: "bold" }}>
            <button onClick={toggleSidebar} className="navbar-toggler mx-2" type="button">
                <span className="navbar-toggler-icon"></span>
            </button>

            <a className="navbar-brand" href="/profile">
                <img src={userIcon} width="35" height="35" className="mx-3 d-inline-block align-top" alt="Foto do usuário"/>
                {username}
            </a>

            <a className="navbar-brand ms-auto" href="/home">
                PetBox
                <img src={petboxLogo} width="35" height="35" className="mx-2 d-inline-block align-top" alt="Logo Petbox"
                     style={{
                         filter: "drop-shadow(0 0px 1px rgba(0,0,0,0.2))" +
                             "drop-shadow(0 1px 2px rgba(0,0,0,0.2)) " +
                             "drop-shadow(0 2px 4px rgba(0,0,0,0.2))",
                         transform: "translateZ(0)"
                     }}
                />
            </a>
        </nav>
    );
}

export default Navbar;