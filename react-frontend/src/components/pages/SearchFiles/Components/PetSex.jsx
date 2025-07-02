import React from "react";

function PetSex({value, onChange}) {

    function handleChange(e) {
        onChange(e);
    }

    return (
        <div className="col-md-2 mx-2 my-2">

            <label className="form-label mb-2">Sexo do PET:</label>
            <select
                className="form-select"
                style={{ borderRadius: "1rem" }}
                name="sex"
                value={value}
                onChange={handleChange}
            >
                <option defaultValue>Qualquer</option>
                <option value="MALE">Macho</option>
                <option value="FEMALE">Fêmea</option>
            </select>
        </div>
    );
}

export default PetSex;
