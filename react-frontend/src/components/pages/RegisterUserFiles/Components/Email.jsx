import React, { useState, useEffect } from "react";
import axios from "axios";

const Email = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!value) {
            setError(null);
            return;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailRegex.test(value)) {
            const delay = setTimeout(() => {
                axios.get(`http://localhost:8080/api/check/email/${value}`)
                .then(resp => {
                    if (resp.data.exists) setError("Email já utilizado");
                    else setError(null);
                })
                .catch(() => setError("Erro ao verificar o email"));
            }, 500);
            return () => clearTimeout(delay);
        }
        else {
            setError("Email inválido");
        }
    }, [value]);

    const inputClass = value === ""
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-6 mx-2 my-2">
            <label htmlFor="email" className="form-label">Email</label>
            <input
                id="email"
                name="email"
                type="email"
                className={inputClass}
                placeholder="Digite seu email"
                value={value}
                onChange={onChange}
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default Email;
