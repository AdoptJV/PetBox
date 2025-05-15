import React, { useState, useEffect } from "react";

const LastName = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    useEffect(() => {
        if (value === "") {
            setError(null);
            return;
        }
        const regex = /^[A-Za-zÀ-ÿ]+(?: [A-Za-zÀ-ÿ]+)*$/;
        setError(regex.test(value) ? null : "Sobrenome inválido");
    }, [value]);

    const inputClass = value === ""
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-4">
            <label htmlFor="inputLastName" className="form-label">Sobrenome</label>
            <input
                name="lastName"
                id="inputLastName"
                type="text"
                className={inputClass}
                placeholder="Digite seu sobrenome"
                value={value}
                onChange={onChange}
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default LastName;
