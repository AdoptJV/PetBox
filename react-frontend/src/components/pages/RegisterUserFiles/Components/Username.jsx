import React, { useState, useEffect } from "react";
import axios from "axios";

const UsernameForm = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!value.trim()) {
            setError(null);
            return;
        }
        const delay = setTimeout(() => {
            axios.get(`http://localhost:8080/api/check/username/${value}`)
                .then(resp => {
                    if (resp.data.exists) setError("Usuário já existe");
                    else setError(null);
                })
                .catch(() => setError("Erro ao verificar o nome de usuário"));
        }, 500);
        return () => clearTimeout(delay);
    }, [value]);

    const cls = !value
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-5 mb-3">
            <label htmlFor="inputUsername" className="form-label">Nome de usuário</label>
            <input
                name="username"
                id="inputUsername"
                type="text"
                className={cls}
                placeholder="Digite seu nome de usuário"
                value={value}
                onChange={onChange}
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default UsernameForm;
