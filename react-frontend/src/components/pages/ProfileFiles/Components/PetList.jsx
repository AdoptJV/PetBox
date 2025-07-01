import PetCard from "../../../general/PetCard.jsx";
import {useEffect, useState} from "react";

function PetList({username}) {
    const [pets, setPets] = useState(null);
    console.log(username)
    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const res = await fetch("http://localhost:8080/api/profilepets", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({username: username}),
                });

                if (!res.ok) {
                    console.error("Server error", res.status);
                    return;
                }

                const data = await res.json();
                console.log("Received data:", data);
                setPets(data);
            } catch (e) {
                console.error("Fetch failed:", e);
            }
        }

        fetchUserInfo();
    }, [username]);

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