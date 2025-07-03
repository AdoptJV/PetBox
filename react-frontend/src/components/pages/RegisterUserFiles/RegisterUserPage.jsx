import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Name from "./Components/Name.jsx";
import LastName from "./Components/LastName.jsx";
import Birthdate from "./Components/Birthdate.jsx";
import Phone from "./Components/Phone.jsx";
import Email from "./Components/Email.jsx";
import Username from "./Components/Username.jsx";
import Password from "./Components/Password.jsx";
import Cep from "./Components/Cep.jsx";
import UseTerms from "./Components/UseTerms.jsx";
import clouds from "../../../assets/clouds.png";
import UserPfp from "./Components/UserPfp.jsx";

function RegisterUserPage() {
    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        birthdate: "",
        phone: "",
        email: "",
        username: "",
        password: "",
        cep: "",
        terms: false,
    });
    const [response, setResponse] = useState("");
    const navigate = useNavigate();
    const [profilePicture, setProfilePicture] = useState(null);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formPayload = new FormData();
            for (const key in formData) {
                formPayload.append(key, formData[key]);
            }
            if (profilePicture) {
                formPayload.append("profilePicture", profilePicture);
            }

            const res = await fetch("http://localhost:8080/api/register-user", {
                method: "POST",
                body: formPayload,
            });

            const data = await res.json();

            if (res.ok) {
                navigate("/");
            } else {
                setResponse(`Erro: ${data.message || "Problema no servidor"}`);
            }
        } catch {
            setResponse("Erro ao enviar dados para o servidor.");
        }
    };

    return (
        <>
            {/* Fonte Poppins importada */}
            <link
                href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap"
                rel="stylesheet"
            />
            <style>{`
        html, body {
          margin: 0; padding: 0; height: 100%; overflow-x: hidden;
          font-family: 'Poppins', sans-serif;
          background-color: #1e1e1e; /* Fundo escuro */
        }
        @keyframes fadeUp {
          0% {
            opacity: 0;
            transform: translateY(20px);
          }
          100% {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>

            <div
                className="d-flex justify-content-center"
                style={{
                    position: "relative", // para overlay
                    backgroundColor: "#a1c8ff", // fundo escuro
                    backgroundImage: `url(${clouds})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    backgroundRepeat: "no-repeat",
                    minHeight: "100vh",
                    fontFamily: "'Poppins', sans-serif",
                    padding: "1rem",
                    overflowY: "hidden",
                }}
            >
                {/* Overlay preta semitransparente para escurecer suavemente */}
                <div
                    style={{
                        position: "absolute",
                        backgroundColor: "rgba(0, 0, 0, 0.25)",

                        inset: 0,
                        zIndex: 0,
                    }}
                />

                {/* Conteúdo da página com zIndex maior */}
                <div
                    className="my-5 py-2 w-50 card shadow rounded-4"
                    style={{
                        position: "relative",
                        zIndex: 1,
                        backgroundColor: "rgba(243, 243, 243, 0.95)",
                        animation: "fadeUp 0.7s ease forwards",
                    }}
                >
                    <form className="container" onSubmit={handleSubmit}>
                        <div className="d-flex flex-row justify-content-start flex-wrap">
                            <Name value={formData.name} onChange={handleChange} />
                            <LastName value={formData.lastName} onChange={handleChange} />
                            <Birthdate value={formData.birthdate} onChange={handleChange} />
                            <Phone value={formData.phone} onChange={handleChange} />
                            <Email value={formData.email} onChange={handleChange} />
                            <Username value={formData.username} onChange={handleChange} />
                            <Password value={formData.password} onChange={handleChange} />
                            <UserPfp onChange={setProfilePicture} />
                        </div>
                        <Cep value={formData.cep} onChange={handleChange} />
                        <UseTerms />

                        <div className="form-check my-2">
                            <input
                                className="form-check-input"
                                type="checkbox"
                                id="Termos"
                                name="terms"
                                checked={formData.terms}
                                onChange={handleChange}
                            />
                            <label className="form-check-label" htmlFor="Termos">
                                Aceito os termos
                            </label>
                        </div>
                        <div className="my-3 d-flex justify-content-center">
                            <button type="submit" className="btn btn-primary">
                                Registrar
                            </button>
                        </div>
                        {response && (
                            <div className="alert alert-info mt-3">{response}</div>
                        )}
                    </form>
                </div>
            </div>
        </>
    );
}

export default RegisterUserPage;
