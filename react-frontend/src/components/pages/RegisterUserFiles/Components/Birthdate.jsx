import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { registerLocale } from "react-datepicker";
import ptBR from "date-fns/locale/pt-BR";
registerLocale("pt-BR", ptBR);

const Birthdate = ({ value, onChange }) => {
    const [error, setError] = useState(null);

    // valida idade ≥18 sempre que value mudar
    useEffect(() => {
        if (!value) {
            setError(null);
            return;
        }
        const date = new Date(value);
        const today = new Date();
        let age = today.getFullYear() - date.getFullYear();
        const m = today.getMonth() - date.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < date.getDate())) {
            age--;
        }
        setError(age < 18 ? "Você deve ter pelo menos 18 anos." : null);
    }, [value]);

    const handle = (date) => {
        // dispara onChange para o pai, convertendo para ISO-string
        onChange({ target: { name: "birthdate", value: date ? date.toISOString() : "" } });
    };

    return (
        <div className="col-md-3 mb-3">
            <label htmlFor="birthdate" className="form-label">Data de Nascimento</label>
            <DatePicker
                id="birthdate"
                name="birthdate"
                selected={value ? new Date(value) : null}
                onChange={handle}
                dateFormat="dd/MM/yyyy"
                className={`form-control rounded-4 ${error ? "is-invalid" : value ? "is-valid" : ""}`}
                placeholderText="dd/mm/aaaa"
                locale="pt-BR"
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};

export default Birthdate;
