// file: Components/Name.jsx
import React, { useState } from "react";

const Name = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    const handleName = (e) => {
        const v = e.target.value;
        onChange(e);

        // agora faz a validação local
        const regex = /^[A-Za-zÀ-ÿ]+$/;
        if (v === "") {
            setError(null);
        } else if (!regex.test(v)) {
            setError("Nome inválido");
        } else {
            setError(null);
        }
    };

    const inputClass = value === ""
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-4">
            <label htmlFor="inputName" className="form-label">Nome</label>
            <input
                name="name"               // <<< precisa ter o mesmo nome que formData
                id="inputName"
                type="text"
                className={inputClass}
                placeholder="Digite seu nome"
                value={value}             // <<< valor vindo do pai
                onChange={handleName}      // <<< evento delegando ao pai + validação
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default Name;
