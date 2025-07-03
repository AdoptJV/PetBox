import React from "react";
import clouds from "../../../assets/clouds.png";

function CloudsBackgroundPage() {
    return (
        <div
            style={{
                height: "100vh",
                width: "100vw",
                backgroundImage: `url(${clouds})`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                backgroundRepeat: "no-repeat",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                flexDirection: "column",
                color: "#000000", // azul céu suave
                textShadow: "0 0 8px rgba(50, 100, 255, 0.7)", // sombra azulada suave
                fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
                fontSize: "2.5rem",
                userSelect: "none",
            }}
        >
            <h1
                style={{
                    animation: "pulse 3s ease-in-out infinite",
                    marginBottom: "1rem",
                }}
            >
                🚧 Em Construção... 🚧
            </h1>
            <p
                style={{
                    fontSize: "1.2rem",
                    maxWidth: "400px",
                    textAlign: "center",
                    animation: "fadeInOut 6s ease-in-out infinite",
                }}
            >
                Estamos preparando algo incrível para você! <br /> Volte em breve para
                ver as novidades nas nuvens.
            </p>

            <style>
                {`
          @keyframes pulse {
            0%, 100% {
              opacity: 1;
              transform: scale(1);
            }
            50% {
              opacity: 0.6;
              transform: scale(1.05);
            }
          }

          @keyframes fadeInOut {
            0%, 100% {
              opacity: 0.7;
            }
            50% {
              opacity: 1;
            }
          }
        `}
            </style>
        </div>
    );
}

export default CloudsBackgroundPage;
