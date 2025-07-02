import PetCard from "../../../general/PetCard.jsx";
import { useEffect, useState } from "react";

function PetList({ formData }) {
    const [pets, setPets] = useState(null);

    useEffect(() => {
        if (!formData) return; // ⛔ Prevent fetch before user submits

        async function fetchPets() {
            try {
                const res = await fetch("http://localhost:8080/api/filter", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    credentials: "include",
                    body: JSON.stringify(formData),
                });

                if (!res.ok) {
                    console.error("Erro do servidor:", res.status);
                    setPets([]);
                    return;
                }

                const data = await res.json();
                console.log("Dados recebidos:", data);
                setPets(data);
            } catch (error) {
                console.error("Erro ao buscar pets:", error);
                setPets([]);
            }
        }

        fetchPets();
    }, [formData]);

    return (
        <div className="container-fluid">
            <div className="d-flex flex-wrap justify-content-start" style={{ gap: "1rem" }}>
                {
                    !pets || pets.length === 0 ? (
                        <p className="text-center w-100">PETs não encontrados :(</p>
                    ) : (
                        pets.map((pet, idx) => (
                            <div key={idx} style={{ flex: "1 1 250px", maxWidth: "300px" }}>
                                <PetCard petData={pet} />
                            </div>
                        ))
                    )
                }
            </div>
        </div>
    );
}

export default PetList;
