import { useState } from "react";
import { useNavigate } from "react-router-dom";

/* menu de login */

function LandingMenu() {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    const [response, setResponse] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const redirect = (e) => {
        e.preventDefault();
        navigate("/register"); // redireciona para a pagina de registro de usuario
    }

    async function loginUser(username, password) {
        try {
            const response = await fetch("http://localhost:8080/api/login-user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify({ username, password })
            });

            if (response.ok) { // status 200: sucesso
                const data = await response.json();
                console.log("Login OK:", data.message);
                window.location.href = "/home"; // redireciona
            } else if (response.status === 401) { // senha incorreta
                const data = await response.json();
                console.error("Erro de autenticação:", data.message);
                setError(data.message);
            } else if (response.status === 404) { // usuário não encontrado
                const data = await response.json();
                console.error("Usuário não encontrado:", data.message);
                setError(data.message);
            } else { // outro erro
                console.error("Erro inesperado:", response.status);
            }

        } catch (error) {
            console.error("Erro na requisição:", error);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
            await loginUser(formData.username, formData.password);
    };

    return (
        <div className="container my-5">
            <div className="card shadow rounded-4" style={{ backgroundColor: "#f3f3f3" }}>
                <h5 className="card-title my-3 ">Seja bem-vindo(a) ao PetBox!</h5>
                <div className="card-body ">
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="exampleInputUsername1" className="form-label">
                                Nome de usuário
                            </label>
                            <input
                                type="text"
                                name="username"
                                className="form-control rounded-4"
                                id="exampleInputUsername1"
                                value={formData.username}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="exampleInputPassword1" className="form-label">
                                Senha
                            </label>
                            <input
                                type="password"
                                name="password"
                                className="form-control rounded-4"
                                id="exampleInputPassword1"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </div>
                        <button
                            type="submit"
                            className="btn rounded-2"
                            style={{ backgroundColor: "#c1c1c1"}}
                        >
                            Login
                        </button>
                    </form>
                    <hr />
                    <div className="mb-3">
                        <label className="form-label">Não possui uma conta?</label>
                        <br />
                        <button
                            type="button"
                            className="btn rounded-2"
                            style={{ backgroundColor: "#c1c1c1" }}
                            onClick={redirect}
                        >
                            Registre-se
                        </button>
                    </div>
                    {error && (
                        <div className="alert alert-info mt-3" role="alert">
                            {error}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default LandingMenu;
