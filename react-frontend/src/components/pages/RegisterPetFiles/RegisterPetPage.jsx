import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import PetName from "./Components/PetName.jsx";
import PetAge from "./Components/PetAge.jsx";
import PetSex from "./Components/PetSex.jsx";
import PetSpecie from "./Components/PetSpecie.jsx";
import PetCastrated from "./Components/PetCastrated.jsx";
import PetPfp from "./Components/PetPfp.jsx";
import PetDescription from "./Components/PetDescription.jsx";
import Bars from "../../general/Bars.jsx";
import clouds from "../../../assets/clouds.png";

function RegisterPetPage() {
    const [formData, setFormData] = useState({
        name: "",
        age: "",
        sex: "",
        specie: "",
        castrated: false,
        description: ""
    });
    const [profilePicture, setProfilePicture] = useState(null);
    const [response, setResponse] = useState("");
    const navigate = useNavigate();

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

            const res = await fetch("http://localhost:8080/api/register-pet", {
                method: "POST",
                body: formPayload,
                credentials: "include"
            });

            const data = await res.json();

            if (res.ok) {
                navigate("/home");
            } else {
                setResponse(`Erro: ${data.message || "Problema no servidor"}`);
            }
        } catch {
            setResponse("Erro ao enviar dados para o servidor.");
        }
    };

    return (
        <Bars>
            {/* Adicionando a animação global para o fadeInContent */}
            <style>{`
                @keyframes fadeInContent {
                    from { opacity: 0; transform: translateY(20px); }
                    to { opacity: 1; transform: translateY(0); }
                }
            `}</style>

            <div
                className="d-flex justify-content-center align-items-center"
                style={{
                    position: "relative",  // importante para overlay funcionar
                    backgroundImage: `url(${clouds})`,
                    backgroundColor: "#a1c8ff",
                    minHeight: "calc(100vh - 65px)",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    backgroundRepeat: "no-repeat",
                }}
            >
                {/* Overlay escurecido */}
                <div
                    style={{
                        position: "absolute",
                        inset: 0,
                        backgroundColor: "rgba(110,110,110,0.25)", // mesma cor do landing
                        zIndex: 0,
                    }}
                />

                <div
                    className="card p-4 shadow-lg rounded-4"
                    style={{
                        backgroundColor: "#ffffffcc",
                        maxWidth: "750px",
                        width: "90%",
                        position: "relative", // para ficar acima do overlay
                        zIndex: 1,
                        animation: "fadeInContent 1s ease forwards"  // animação aplicada aqui
                    }}
                >
                    <h2 className="text-center mb-4 fw-bold text-primary">Cadastro do Pet</h2>
                    <form className="container" onSubmit={handleSubmit}>
                        <div className="row gy-3">
                            <div className="col-md-6">
                                <PetName value={formData.name} onChange={handleChange} />
                            </div>
                            <div className="col-md-6">
                                <PetAge value={formData.age} onChange={handleChange} />
                            </div>
                            <div className="col-md-6">
                                <PetSex value={formData.sex} onChange={handleChange} />
                            </div>
                            <div className="col-md-6">
                                <PetSpecie value={formData.specie} onChange={handleChange} />
                            </div>
                            <div className="col-md-6">
                                <PetCastrated value={formData.castrated} onChange={handleChange} />
                            </div>
                            <div className="col-md-6">
                                <PetPfp onChange={setProfilePicture} />
                            </div>
                            <div className="col-12">
                                <PetDescription value={formData.description} onChange={handleChange} />
                            </div>
                        </div>

                        <div className="mt-4 d-flex justify-content-center">
                            <button type="submit" className="btn btn-lg btn-primary px-5">
                                Registrar Pet
                            </button>
                        </div>

                        {response && (
                            <div className="alert alert-warning text-center mt-4">
                                {response}
                            </div>
                        )}
                    </form>
                </div>
            </div>
        </Bars>
    );
}

export default RegisterPetPage;
