import home from "../../../assets/house-heart.svg"
import search from "../../../assets/search-heart.svg"
import chat from "../../../assets/chat-left-heart.svg"
import register from "../../../assets/clipboard-heart.svg"
import ongs from "../../../assets/bookmark-heart.svg"
import gear from "../../../assets/gear.svg"
import logout from "../../../assets/box-arrow-left.svg"
import { useNavigate } from "react-router-dom"
import {useState} from "react";

function Sidebar(bool) {
    let collapsed = bool.collapsed;
    const sidebarWidth = collapsed ? 0 : 250;

    const [response, setResponse] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault(); // 🔐 Prevent full page reload

        try {
            const res = await fetch("http://localhost:8080/api/logout", {
                method: "POST",
                credentials: "include"
            });

            const data = await res.json();
            if (data.redirect) {
                navigate(data.redirect);
            }
            setResponse(`Server echoed: ${data.echo}`);
        } catch (err) {
            console.error(err);
            setResponse("Error sending message");
        }
    };

    return (
        <>
            <link rel="stylesheet"
                  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
            <div style={{display: "flex", height: "100vh"}}>
                {/* Sidebar */}
                <div
                    style={{
                        width: `${sidebarWidth}px`,
                        height: "100%",
                        overflow: "hidden",
                        transition: "width 0.3s ease",
                        background: "#f8f9fa",
                        borderRight: collapsed ? "none" : "1px solid #dee2e6",
                        padding: collapsed ? "0" : "1rem",
                        position: "fixed",
                    }}
                >
                    {/* Inner content with transition */}
                    <div
                        style={{
                            opacity: collapsed ? 0 : 1,
                            transform: collapsed ? "translateX(-20px)" : "translateX(0)",
                            transition: "opacity 0.3s ease, transform 0.3s ease",
                            pointerEvents: collapsed ? "none" : "auto",
                        }}
                    >
                        <ul style={{listStyle: "none", padding: 0, margin: 0}}>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/home')}>
                                    <img src={home} alt={"Home"} className="mx-2"/>HOME</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/search')}>
                                    <img src={search} alt={"Buscar"} className="mx-2"/>BUSCAR</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/chat')}>
                                    <img src={chat} alt={"Chat"} className="mx-2"/>CHATS</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/petregister')}>
                                    <img src={register} alt={"Registrar Pet"} className="mx-2"/>REGISTRAR PET</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/ongs')}>
                                    <img src={ongs} alt={"ONGs"} className="mx-2"/>ONGs</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={() => navigate('/settings')}>
                                    <img src={gear} alt={"Confirgurações"} className="mx-2"/>Configurações</button>
                            </li>
                            <hr/>
                            <li>
                                <button className="btn d-flex align-items-center" onClick={handleSubmit}>
                                    <img src={logout} alt={"Logout"} className="mx-2"/>Logout</button>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Sidebar;
