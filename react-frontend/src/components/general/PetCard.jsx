import { useNavigate } from 'react-router-dom';
import petboxLogo from "../../assets/placeholderPost.jpg"
import {useEffect, useState} from "react";

function PetCard() {
    //++++++++++++++++++++++++ MUDANÇA DE PÁGINA ++++++++++++++++++++++++//

    const navigate = useNavigate();
    const handleCardClick = () => {
        // Navigate to pet details page with pet ID
        navigate("/pets");
    };
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    //++++++++++++++++++++++ PASSAR MOUSE POR CIMA ++++++++++++++++++++++//

    const [hovered, setHovered] = useState(false)

    const hoverStyle = {
        transform: hovered ? "scale(1.02)" : "scale(1)",
        transition: "all 0.3s ease",
        cursor: "pointer",
        width: "15rem",
        boxShadow: hovered ? "0 4px 8px rgba(0,0,0,0.1)" : "0"
    };
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    //+++++++++++++++++++++ PUXAR DO BANCO DE DADOS +++++++++++++++++++++//

    const [petName, setPetName] = useState("");
    const [petURL, setPetURL] = useState("");
    const [petDesc, setPetDesc] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/petcard", {
            credentials: "include",
        })
            .then(res => res.json())
            .then(data => setPetName(data.petname))
            .then(data => setPetURL(data.peturl))
            .then(data => setPetDesc(data.petdesc))
            .catch(() => setPetName(""));
    }, []);
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    return (
        <div onClick={handleCardClick}
             onMouseEnter={() => setHovered(true)}
             onMouseLeave={() => setHovered(false)}
             className="card"
             style={hoverStyle}>
            <h6 className="mx-2 my-2 text-center">Pepeu</h6>
            <img src={petboxLogo} alt="placeholder" style={{objectFit: "cover"}}/>
            <div className="card-body">
                <p className="card-text">
                    Esse gatinho azul foi abandonado na rua Augusta. Muito carinhoso e precisa de um lar.
                </p>
            </div>
        </div>
    )
}

export default PetCard