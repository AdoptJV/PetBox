import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import PetName from "./Components/PetName.jsx";
import PetAge from "./Components/PetAge.jsx";
import PetSex from "./Components/PetSex.jsx";
import PetSpecie from "./Components/PetSpecie.jsx";
import PetCastrated from "./Components/PetCastrated.jsx";
import PetPfp from "./Components/PetPfp.jsx";
// import PetPfp from "./Components/PetPfp.jsx"; // supondo que você vai ter isso também
import clouds from "../../../assets/clouds.png"; // mesma imagem de fundo
import PetDescription from "./Components/PetDescription.jsx";
import Bars from "../../general/Bars.jsx"; // mesma imagem de fundo

function RegisterPetPage() {
    console.log("RegisterPetPage");
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
        console.log(formData);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Pet form submitted:", formData);
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
            <div
                className="d-flex justify-content-center align-items-center"
                style={{
                    backgroundImage: `url(${clouds})`,
                    backgroundColor: "#a1c8ff",
                    minHeight: "calc(100vh - 65px)",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    backgroundRepeat: "no-repeat"
                }}
            >
                <div
                    className="py-4 w-50 card shadow rounded-4"
                    style={{ backgroundColor: "#f3f3f3" }}
                >
                    <form className="container" onSubmit={handleSubmit}>
                        <div className="d-flex flex-row justify-content-start flex-wrap">
                            <PetName value={formData.name} onChange={handleChange} />
                            <PetAge value={formData.age} onChange={handleChange} />
                            <PetSex value={formData.sex} onChange={handleChange} />
                            <PetSpecie value={formData.specie} onChange={handleChange} />
                            <PetCastrated value={formData.castrated} onChange={handleChange} />
                            <PetDescription value={formData.description} onChange={handleChange} />
                            <PetPfp onChange={setProfilePicture} />
                        </div>
                        
                        <div className="my-3 d-flex justify-content-center">
                            <button type="submit" className="btn btn-primary">Registrar Pet</button>
                        </div>

                        {response && <div className="alert alert-info mt-3">{response}</div>}
                    </form>
                </div>
            </div>
        </Bars>
    );

}

export default RegisterPetPage;