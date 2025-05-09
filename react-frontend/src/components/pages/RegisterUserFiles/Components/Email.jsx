import React, { useState, useEffect } from "react";

const Email = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!value) {
            setError(null);
            return;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        setError(emailRegex.test(value) ? null : "Email inválido");
    }, [value]);

    const inputClass = value === ""
        ? "form-control rounded-4"
        : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="col-md-6">
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
