import petboxLogo from "../../assets/smallLogo.svg";
import userIcon from "../../assets/person-circle-white.svg";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Navbar({ collapsed, toggleSidebar }) {
    const [username, setUsername] = useState("");
    const [userId, setUserId] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/nav", {
            credentials: "include",
        })
            .then(res => res.json())
            .then(data => {
                setUsername(data.username || "");
                setUserId(data.userId || "");
            })
            .catch(() => {
                setUsername("");
                setUserId("");
            });
    }, []);

    return (
        <nav className="navbar navbar-dark sticky-top shadow-sm"
             style={{
                 backgroundColor: "#6ea8fe",
                 fontWeight: "bold",
                 paddingLeft: "1rem",
                 paddingRight: "1rem"
             }}
        >
            <div className="d-flex align-items-center">
                <button onClick={toggleSidebar} className="navbar-toggler me-2" type="button">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <a className="navbar-brand d-flex align-items-center" href="/home">
                    <img
                        src={petboxLogo}
                        width="35"
                        height="35"
                        className="me-2"
                        alt="Logo Petbox"
                        style={{
                            filter: "drop-shadow(0 1px 2px rgba(0,0,0,0.3))",
                            transform: "translateZ(0)"
                        }}
                    />
                    <span className="fs-5 text-white">PetBox</span>
                </a>
            </div>

            <Link
                className="navbar-brand d-flex align-items-center ms-auto"
                to={`/profile/${userId}`}
            >
                <img
                    src={`http://localhost:8080/api/pfps/${username}_pfp.jpg`}
                    onError={(e) => {
                        e.currentTarget.onerror = null;
                        e.currentTarget.src = userIcon;
                    }}
                    alt="Foto do usuário"
                    className="rounded-circle shadow-sm me-2"
                    style={{
                        width: "35px",
                        height: "35px",
                        objectFit: "cover",
                        border: "2px solid white"
                    }}
                />
                <span className="text-white text-capitalize">{username}</span>
            </Link>
        </nav>
    );
}

export default Navbar;
