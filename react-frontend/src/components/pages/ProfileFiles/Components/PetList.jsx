import PetCard from "../../../general/PetCard.jsx";
import {useEffect, useState} from "react";

function PetList({username}) {
    const [pets, setPets] = useState("");
    useEffect(() => {
        fetch("http://localhost:8080/api/profilepets", {
            credentials: "include",
        })
            .then(res => {
                if (!res.ok) throw new Error("Erro selecionando pets próximos");
                return res.json();
            })
            .then(data => {
                setPets(data);
            })
            .catch(error => {
                console.error("Erro selecionando pets próximos:", error);
                setPets([]);
            });
    }, []);

    return (
        <div className="container-fluid overflow-x-scroll">
            <div className="d-flex flex-row" style={{ gap: "1rem", minWidth: "max-content" }}>
                {
                    !pets || pets.length === 0 ? (
                        <p className="text-center">{username} não possui PETs</p>
                    ) : (
                        pets.map((pet, idx) => (
                            <div key={idx} style={{ flex: "0 0 auto" }}>
                                <PetCard petData={pet} />
                            </div>
                        ))
                    )
                }
            </div>
        </div>
    );

}

export default PetList