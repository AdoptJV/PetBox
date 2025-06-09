import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Name from "./Components/Name.jsx";
import LastName from "./Components/LastName.jsx";
import Birthdate from "./Components/Birthdate.jsx";
import Phone from "./Components/Phone.jsx";
import Email from "./Components/Email.jsx";
import Username from "./Components/Username.jsx";
import Password from "./Components/Password.jsx";
import ProfilePicture from "./Components/ProfilePicture.jsx";
import Cep from "./Components/Cep.jsx";
import UseTerms from "./Components/UseTerms.jsx";
import clouds from "../../../assets/clouds.png";

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
            const text = await res.text();
            let data;
            try {
                data = JSON.parse(text);
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
        <div className="d-flex justify-content-center" style={{
            backgroundImage: `url(${clouds})`,
            backgroundColor: "#a1c8ff",
            minHeight: "100vh",
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat"
        }}>
            <div className="my-5 py-2 w-50 card shadow rounded-4" style={{ backgroundColor: "#f3f3f3" }}>
                <form className="container" onSubmit={handleSubmit}>
                    <div className="d-flex flex-row justify-content-start flex-wrap">
                        <Name value={formData.name} onChange={handleChange}/>
                        <LastName value={formData.lastName} onChange={handleChange}/>
                        <Birthdate value={formData.birthdate} onChange={handleChange}/>
                        <Phone value={formData.phone} onChange={handleChange}/>
                        <Email value={formData.email} onChange={handleChange}/>
                        <Username value={formData.username} onChange={handleChange}/>
                        <Password value={formData.password} onChange={handleChange}/>
                        <ProfilePicture/>
                    </div>
                    <Cep value={formData.cep} onChange={handleChange}/>
                    <UseTerms/>

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
                        <button type="submit" className="btn btn-primary">Registrar</button>
                    </div>
                    {response && <div className="alert alert-info mt-3">{response}</div>}
                </form>
            </div>
        </div>
    );
}

export default RegisterUserPage;
