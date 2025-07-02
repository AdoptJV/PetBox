// src/components/pages/RegisterPetFiles/Components/PetDescription.jsx
import React, { useState } from "react";

function PetDescription({ value, onChange }) {
    const [error, setError] = useState(null);

    const handleDescription = e => {
        const v = e.target.value;
        onChange(e);

        // (opcional) validação simples: no máximo 200 caracteres
        if (v.length > 200) {
            setError("Máximo de 200 caracteres");
        } else {
            setError(null);
        }
    };

    const textareaClass = `form-control rounded-4 ${error ? "is-invalid" : ""}`;

    return (
        <div className="col-12 mx-2 my-2">
            <label htmlFor="inputDescription" className="form-label">Descrição</label>
            <textarea
                name="description"
                id="inputDescription"
                className={textareaClass}
                rows={3}
                placeholder="Conte um pouco sobre o seu pet"
                value={value}
                onChange={handleDescription}
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
}

export default PetDescription;
