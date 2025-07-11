import React, { useState, useEffect } from "react";
import axios from "axios";

const States = [ /* ...seus estados */ ];

const Cep = ({ value, onChange }) => {
    const [endereco, setEndereco] = useState(null);
    const [error, setError] = useState(null);

    // sempre que o CEP mudar e tiver 8 dígitos, busca o endereço
    useEffect(() => {
        const v = value.replace(/\D/g, "");
        if (v.length === 8) {
            axios.get(`http://localhost:8080/api/cep-info/${v}`)
                .then(r => {
                    setEndereco(r.data);
                    setError(null);
                })
                .catch(() => {
                    setEndereco(null);
                    setError("CEP não encontrado");
                });
        } else {
            setEndereco(null);
            setError(null);
        }
    }, [value]);

    return (
        <>
            <div className="mx-2 my-2 col-md-5">
                <label htmlFor="cep" className="form-label">CEP</label>
                <input
                    id="cep"
                    name="cep"
                    type="text"
                    maxLength={9}
                    className={`form-control rounded-4 ${error ? "is-invalid" : ""}`}
                    placeholder="Digite o CEP"
                    value={value}
                    onChange={onChange}
                />
                {error && <div className="invalid-feedback d-block">{error}</div>}
            </div>
            {endereco && (
                <div className="d-flex flex-row justify-content-start flex-wrap">
                    <div className="mx-2 my-2 col-md-5">
                        <label className="form-label">Logradouro</label>
                        <input className="form-control rounded-4" readOnly value={endereco.logradouro} />
                    </div>
                    <div className="mx-2 my-2 col-md-5">
                        <label className="form-label">Bairro</label>
                        <input className="form-control rounded-4" readOnly value={endereco.bairro} />
                    </div>
                    <div className="mx-2 my-2 col-md-5">
                        <label className="form-label">Cidade</label>
                        <input className="form-control rounded-4" readOnly value={endereco.localidade} />
                    </div>
                    <div className="mx-2 my-2 col-md-5">
                        <label className="form-label">Estado</label>
                        <input className="form-control rounded-4" readOnly value={endereco.uf} />
                    </div>
                </div>
            )}
        </>
    );
};

export default Cep;
