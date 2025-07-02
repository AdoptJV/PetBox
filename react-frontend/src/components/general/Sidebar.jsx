import home from "../../assets/house-heart.svg";
import search from "../../assets/search-heart.svg";
import chat from "../../assets/chat-left-heart.svg";
import register from "../../assets/clipboard-heart.svg";
import ongs from "../../assets/bookmark-heart.svg";
import gear from "../../assets/gear.svg";
import logout from "../../assets/box-arrow-left.svg";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function Sidebar({ collapsed }) {
    const sidebarWidth = collapsed ? 0 : 250;
    const [response, setResponse] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch("http://localhost:8080/api/logout", {
                method: "POST",
                credentials: "include",
            });
            const data = await res.json();
            if (data.redirect) navigate(data.redirect);
            setResponse(`Server echoed: ${data.echo}`);
            navigate("/");
        } catch (err) {
            console.error(err);
            setResponse("Error sending message");
        }
    };

    const menuItems = [
        { label: "Home", icon: home, path: "/home" },
        { label: "Buscar", icon: search, path: "/search" },
        { label: "Chats", icon: chat, path: "/chat" },
        { label: "Registrar Pet", icon: register, path: "/pet-register" },
        { label: "ONGs", icon: ongs, path: "/ongs" },
        { label: "Configurações", icon: gear, path: "/settings" },
    ];

    return (
        <div
            style={{
                width: `${sidebarWidth}px`,
                height: "100vh",
                overflow: "hidden",
                transition: "width 0.3s ease",
                background: "#fdfdfe",
                borderRight: collapsed ? "none" : "1px solid #dee2e6",
                padding: collapsed ? "0" : "1.2rem 0.8rem",
                position: "fixed",
                boxShadow: collapsed ? "none" : "2px 0 8px rgba(0,0,0,0.05)",
                zIndex: 1000,
            }}
        >
            <div
                style={{
                    opacity: collapsed ? 0 : 1,
                    transform: collapsed ? "translateX(-20px)" : "translateX(0)",
                    transition: "opacity 0.3s ease, transform 0.3s ease",
                    pointerEvents: collapsed ? "none" : "auto",
                }}
            >
                <ul className="list-unstyled">
                    {menuItems.map((item, index) => (
                        <li key={index} className="my-2">
                            <button
                                className="btn w-100 d-flex align-items-center sidebar-btn"
                                onClick={() => navigate(item.path)}
                            >
                                <img src={item.icon} alt={item.label} className="me-3" width="24" />
                                <span className="fs-6">{item.label}</span>
                            </button>
                        </li>
                    ))}
                    <hr className="my-3" />
                    <li>
                        <button
                            className="btn w-100 d-flex align-items-center text-danger sidebar-btn"
                            onClick={handleSubmit}
                        >
                            <img src={logout} alt="Logout" className="me-3" width="24" />
                            <span className="fs-6">Logout</span>
                        </button>
                    </li>
                </ul>
            </div>

            {/* Custom styling */}
            <style>
                {`
                    .sidebar-btn {
                        border-radius: 12px;
                        padding: 0.6rem 0.8rem;
                        transition: background-color 0.2s, transform 0.2s;
                        text-align: left;
                    }

                    .sidebar-btn:hover {
                        background-color: #e9f2ff;
                        transform: scale(1.02);
                    }
                `}
            </style>
        </div>
    );
}

export default Sidebar;
