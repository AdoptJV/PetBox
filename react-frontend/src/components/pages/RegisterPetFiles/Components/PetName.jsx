import React, {useState} from "react";

function PetName({value, onChange}) {
    const [error, setError] = useState(null);

    const handleName = (e) => {
        const v = e.target.value;

        onChange(e);

        const regex = /^([a-zA-ZÀ-ÿ]+)( [a-zA-ZÀ-ÿ]+)*$/;
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
        <div className="col-md-5 mx-2 my-2">
            <label htmlFor="inputName" className="form-label">Nome do Pet</label>
            <input
                name="name"
                id="inputName"
                type="text"
                className={inputClass}
                placeholder="Digite o nome do Pet"
                value={value}
                onChange={handleName}
                required
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );

}

export default PetName;