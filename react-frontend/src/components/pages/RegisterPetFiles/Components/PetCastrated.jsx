import React from "react";

function PetCastrated({ value, onChange }) {
    function handleChange(e) {
        onChange(e);
    }

    return (
        <div className="col-md-5 mx-2 my-2">
            <label className="form-label mb-2 ps-2">Castração</label>
            <select
                className="form-select ps-2"
                style={{ borderRadius: "1rem" }}
                name="castrated"
                value={value}
                onChange={handleChange}
            >
                <option defaultValue>Selecione</option>
                <option value="FALSE">Não castrado</option>
                <option value="TRUE">Castrado</option>
            </select>
        </div>
    );
}

export default PetCastrated;
