import Navbar from "../../functions/general/Navbar.jsx";
import React, {useEffect, useState} from "react";
import {redirect, useNavigate} from "react-router-dom";

import PetAge from "./Components/PetAge.jsx";
import PetSpecie from "./Components/PetSpecie.jsx";
import PetCastrated from "./Components/PetCastrated.jsx";
import PetSex from "./Components/PetSex.jsx";
import PetName from "./Components/PetName.jsx";


function RegisterPetPage() {
    const [formData, setFormData] = useState({
        name: "",
        sex: "",
        age: "",
        castrated: "",
        specie: "",
        race: ""
    });
    const [response, setResponse] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    useEffect(() => {
        console.log(formData);
    }, [formData]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
        try {
            const res = await fetch("http://localhost:8080/api/register-pet", {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });
            try {
                if (res.status === 200) {
                    redirect("/home")
                }
                else {
                    setResponse("Erro ao processar a resposta do servidor.");
                    return;
                    console.error()
                }
            } catch {
                setResponse("Erro ao processar a resposta do servidor.");
                return;
            }
            if (data.redirect) navigate(data.redirect);
            else setResponse(`Erro: ${data.message || "Problema no servidor"}`);
        } catch {
            setResponse("Erro ao enviar dados para o servidor.");
        }
    };

    return (
        <form className="container mt-5" onSubmit={handleSubmit}>
            <div className="row mb-3">
                <PetName // value={formData.name} onChange={handleChange} />
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                />
            </div>
            <div className="row mb-3">
                <PetAge
                    name="age"
                    value={formData.age}
                    onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <PetSex
                    name="sex"
                    value={formData.sex}
                    onChange={handleChange}
                />
            </div>
            <div className="row mb-3">
                <PetCastrated
                    name="castrated"
                    value={formData.castrated}
                    onChange={handleChange} />
            </div>

            <div className="row mb-3">
                <PetSpecie
                    specie={formData.specie}
                    race={formData.race}
                    onChange={handleChange}
                />
            </div>

            <button type="submit" className="btn btn-primary">
                Registrar
            </button>
            {response && <div className="alert alert-info mt-3">{response}</div>}
        </form>
    );
}

export default RegisterPetPage;