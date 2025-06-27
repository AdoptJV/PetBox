import React, { useState, useEffect } from "react";

const Phone = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    useEffect(() => {
        if (value === "") {
            setError(null);
            return;
        }
        // Ex: permite apenas dígitos e tamanho mínimo 8
        const cleaned = value.replace(/\D/g, "");
        setError(cleaned.length >= 8 ? null : "Telefone inválido");
    }, [value]);

    const inputClass = value === ""
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-4 mx-2 my-2">
            <label htmlFor="inputPhone" className="form-label">Telefone</label>
            <input
                name="phone"
                id="inputPhone"
                type="text"
                className={inputClass}
                placeholder="Digite seu telefone"
                value={value}
                onChange={onChange}
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default Phone;
