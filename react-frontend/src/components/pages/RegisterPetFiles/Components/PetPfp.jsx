import React from "react";

function PetPfp({ onChange }) {
    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            onChange(file);
        }
    };

    return (
        <div className="form-group w-100">
            <label htmlFor="petPfp" className="form-label mb-2">Foto do Pet</label>
            <input
                type="file"
                id="petPfp"
                accept="image/*"
                className="form-control"
                onChange={handleFileChange}
                style={{
                    borderRadius: "1rem", // deixa arredondado igual aos outros inputs
                    padding: "0.375rem 0.75rem", // padding igual ao padrão do Bootstrap
                    height: "38px", // altura coerente com outros campos
                    lineHeight: "1.5"
                }}
            />
        </div>
    );
}

export default PetPfp;
