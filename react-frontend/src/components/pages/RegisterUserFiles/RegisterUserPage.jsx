import React, { useState } from "react";
import {redirect, useNavigate} from "react-router-dom";
import Name from "./Components/Name.jsx";
import LastName from "./Components/LastName.jsx";
import Birthdate from "./Components/Birthdate.jsx";
import Phone from "./Components/Phone.jsx";
import Email from "./Components/Email.jsx";
import Username from "./Components/Username.jsx";
import Password from "./Components/Password.jsx";
import Cep from "./Components/Cep.jsx";
import UseTerms from "./Components/UseTerms.jsx";

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

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
        try {
            const res = await fetch("http://localhost:8080/api/register-user", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
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
        <form className="container mt-5" onSubmit={handleSubmit}>
            <div className="row mb-3">
                <Name value={formData.name} onChange={handleChange} />
                <LastName value={formData.lastName} onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <Birthdate value={formData.birthdate} onChange={handleChange} />
                <Phone value={formData.phone} onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <Email value={formData.email} onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <Username value={formData.username} onChange={handleChange} />
                <Password value={formData.password} onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <Cep value={formData.cep} onChange={handleChange} />
            </div>
            <div className="row mb-3">
                <UseTerms />
                <div className="form-check">
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
            </div>
            <button type="submit" className="btn btn-primary">
                Registrar
            </button>
            {response && <div className="alert alert-info mt-3">{response}</div>}
        </form>
    );
}

export default RegisterUserPage;
