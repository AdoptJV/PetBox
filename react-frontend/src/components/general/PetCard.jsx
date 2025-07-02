import { useNavigate } from 'react-router-dom';
import {useState} from "react";

function PetCard({ petData }) {
    const navigate = useNavigate();

    const handleCardClick = () => {
        // Navigate to pet details page with pet ID
        navigate(`/pets/${petData.id}`);
    };

    const [hovered, setHovered] = useState(false)

    const hoverStyle = {
        transform: hovered ? "scale(1.02)" : "scale(1)",
        transition: "all 0.3s ease",
        cursor: "pointer",
        width: "15rem",
        boxShadow: hovered ? "0 4px 8px rgba(0,0,0,0.1)" : "0"
    };

    console.log(petData)


    return (
        <div onClick={handleCardClick}
             onMouseEnter={() => setHovered(true)}
             onMouseLeave={() => setHovered(false)}
             className="card"
             style={hoverStyle}>
            <h6 className="mx-2 my-2 text-center">{petData.name}</h6>
            <p className="mx-2 my-2 text-center">{petData.species}</p>
            <img src={`http://localhost:8080/api/petimg/${petData.id}_pfp.jpg`} alt={`Imagem de ${petData.id}`} className="text-center" style={{objectFit: "cover"}}/>
            <div className="card-body">
                <p className="card-text">{petData.description}</p>
            </div>
        </div>
    )
}

export default PetCard