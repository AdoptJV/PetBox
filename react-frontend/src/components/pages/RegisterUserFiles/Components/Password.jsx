// Components/Password.jsx
import React, { useEffect, useState } from "react";

// Alterado para desestruturar `value` em vez de `password`, alinhando com os demais componentes
const Password = ({ value, onChange }) => {
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordError, setPasswordError] = useState(null);
    const [confirmError, setConfirmError] = useState(null);

    useEffect(() => {
        const pwd = value || "";

        // Valida senha
        if (pwd === "") {
            setPasswordError(null);
        } else if (pwd.length < 4) {
            setPasswordError("A senha deve ter pelo menos 4 caracteres.");
        } else {
            setPasswordError(null);
        }

        // Valida confirmação
        if (confirmPassword.trim() === "" && pwd.length >= 4) {
            setConfirmError("Confirme a senha");
        } else if (pwd !== confirmPassword) {
            setConfirmError("As senhas não coincidem.");
        } else {
            setConfirmError(null);
        }
    }, [value, confirmPassword]);

    const inputClass = (field, error) =>
        field === ""
            ? "form-control rounded-4"
            : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="row">
            <div className="mx-2 my-2 col-md-4">
                <label htmlFor="inputPassword" className="form-label">
                    Senha
                </label>
                <input
                    type="password"
                    name="password"                  // garante que o pai atualize formData.password
                    className={inputClass(value, passwordError)}
                    id="Password"
                    placeholder="Digite sua senha"
                    value={value}
                    onChange={onChange}
                    required
                    minLength={4}
                    maxLength={50}
                />
                {passwordError && (
                    <div className="invalid-feedback d-block">{passwordError}</div>
                )}
            </div>

            <div className="mx-2 my-2 col-md-4">
                <label htmlFor="inputPasswordConfirm" className="form-label">
                    Confirme a Senha
                </label>
                <input
                    type="password"
                    name="confirmPassword"           // nome para controle interno, não enviado ao servidor
                    className={inputClass(confirmPassword, confirmError)}
                    id="PasswordConfirm"
                    placeholder="Digite a senha novamente"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    minLength={4}
                    maxLength={50}
                />
                {confirmError && (
                    <div className="invalid-feedback d-block">{confirmError}</div>
                )}
            </div>
        </div>
    );
};

export default Password;
