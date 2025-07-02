import React, { useState } from "react";

function PetPfp({ onChange }) {
    const [previewUrl, setPreviewUrl] = useState(null);
    const [error, setError] = useState(null);

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (!file) return;

        if (!file.type.startsWith("image/")) {
            setError("Arquivo inválido. Envie uma imagem.");
            setPreviewUrl(null);
            return;
        }

        setError(null);
        onChange(file);

        const reader = new FileReader();
        reader.onloadend = () => {
            setPreviewUrl(reader.result);
        };
        reader.readAsDataURL(file);
    };

    return (
        <div className="col-md-5 mx-2 my-2">
            <label htmlFor="inputPetPfp" className="form-label">Foto do Pet</label>
            <input
                type="file"
                className={`form-control rounded-4 ${error ? "is-invalid" : ""}`}
                id="inputPetPfp"
                accept="image/*"
                onChange={handleFileChange}
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}

            {previewUrl && (
                <div
                    className="mt-2 border rounded"
                    style={{
                        width: "150px",
                        height: "150px",
                        overflow: "hidden",
                        objectFit: "cover",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        backgroundColor: "#f8f9fa",
                    }}
                >
                    <img
                        src={previewUrl}
                        alt="Pet Preview"
                        style={{
                            width: "100%",
                            height: "100%",
                            objectFit: "cover",
                        }}
                    />
                </div>
            )}
        </div>
    );
}

export default PetPfp;
