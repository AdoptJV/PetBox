import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LandingMenu() {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    const [response, setResponse] = useState("");
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
        navigate("/register");
    }

    const handleSubmit = async (e) => {
        e.preventDefault(); // 🔐 Prevent full page reload
        console.log("Form submitted:", formData);

        try {
            const res = await fetch("http://localhost:8080/api/login-user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(formData), // 👍 sending full formData
            });

            const data = await res.json();
            if (data.redirect) {
                navigate(data.redirect);
            }
            setResponse(`Server echoed: ${data.echo}`);
        } catch (err) {
            console.error(err);
            setResponse("Error sending message");
        }
    };

    return (
        <div className="container my-5">1
            <div className="card shadow" style={{ backgroundColor: "#f3f3f3" }}>
                <h5 className="card-title my-3">Seja bem-vindo(a) ao PetBox!</h5>
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="exampleInputUsername1" className="form-label">
                                Nome de usuário
                            </label>
                            <input
                                type="text"
                                name="username"
                                className="form-control"
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
                                className="form-control"
                                id="exampleInputPassword1"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </div>
                        <button
                            type="submit"
                            className="btn"
                            style={{ backgroundColor: "#c1c1c1" }}
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
                            className="btn"
                            style={{ backgroundColor: "#c1c1c1" }}
                            onClick={redirect}
                        >
                            Registre-se
                        </button>
                    </div>
                    {response && (
                        <div className="alert alert-info mt-3" role="alert">
                            {response}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default LandingMenu;
