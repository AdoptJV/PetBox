import PetCard from "./PetCard.jsx";
import { useEffect, useState } from "react";

function HorizontalList() {
    const [pets, setPets] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/api/homepets", {
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
        <div
            className="container-fluid"
            style={{
                padding: "1rem 0",
                backgroundColor: "#f7fbff",
                borderRadius: "1rem",
                boxShadow: "0 4px 10px rgba(161, 200, 255, 0.3)",
                overflowX: "auto",
                scrollbarWidth: "thin",
                scrollbarColor: "#a1c8ff transparent",
            }}
        >
            <div
                className="d-flex flex-row align-items-center"
                style={{
                    gap: "1.25rem",
                    minWidth: "max-content",
                    paddingLeft: "1rem",
                    paddingRight: "1rem",
                }}
            >
                {!pets || pets.length === 0 ? (
                    <p
                        className="text-center"
                        style={{
                            width: "100%",
                            fontStyle: "italic",
                            color: "#777",
                            userSelect: "none",
                        }}
                    >
                        Não há PETs próximos de você :(
                    </p>
                ) : (
                    pets.map((pet, idx) => (
                        <div key={idx} style={{ flex: "0 0 auto" }}>
                            <PetCard petData={pet} />
                        </div>
                    ))
                )}
            </div>
            <style>{`
        /* Scrollbar para navegadores WebKit */
        div::-webkit-scrollbar {
          height: 8px;
        }
        div::-webkit-scrollbar-track {
          background: transparent;
        }
        div::-webkit-scrollbar-thumb {
          background-color: #a1c8ff;
          border-radius: 10px;
        }
      `}</style>
        </div>
    );
}

export default HorizontalList;
