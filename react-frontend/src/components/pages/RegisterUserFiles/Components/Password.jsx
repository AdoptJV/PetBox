import React, { useEffect, useState } from "react";

const Password = ({password, confirmPassword, onChange}) => {
    const [psw, setPsw] = useState("");
    const [confirmPsw, setConfirmPsw] = useState("");

    const [passwordError, setPasswordError] = useState(null);
    const [confirmError, setConfirmError] = useState(null);

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            // Verifica a senha
            if (psw.trim() === "") {
                setPasswordError(null);
            } else if (password.length < 4) {
                setPasswordError("A senha deve ter pelo menos 4 caracteres.");
            } else {
                setPasswordError(null);
            }

            // Verifica a confirmação
            if (confirmPassword.trim() === "" && password.length >= 4) {
                setConfirmError("Confirme a senha");
            } else if (password !== confirmPassword) {
                setConfirmError("As senhas não coincidem.");
            } else {
                password = psw
                setConfirmError(null);
            }
        }, 500);

        return () => clearTimeout(delayDebounceFn);
    }, [password, confirmPassword]);

    const inputClass = (field, error) =>
        field === ""
            ? "form-control rounded-4"
            : `form-control rounded-4 ${error ? "is-invalid" : "is-valid"}`;

    return (
        <div className="row mb-3">
            <div className="col-md-4">
                <label htmlFor="inputPassword" className="form-label">Senha</label>
                <input
                    type="password"
                    className={inputClass(password, passwordError)}
                    id="Password"
                    placeholder="Digite sua senha"
                    value={password}
                    onChange={(e) => setPsw(e.target.value)}
                    required
                    minLength={4}
                    maxLength={50}
                />
                {passwordError && (
                    <div className="invalid-feedback d-block">{passwordError}</div>
                )}
            </div>

            <div className="col-md-4">
                <label htmlFor="inputPasswordConfirm" className="form-label">Confirme a Senha</label>
                <input
                    type="password"
                    className={inputClass(confirmPassword, confirmError)}
                    id="PasswordConfirm"
                    placeholder="Digite a senha novamente"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPsw(e.target.value)}
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
