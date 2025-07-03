import React from "react";
import LandingLogo from "./Components/LandingLogo.jsx";
import LandingMenu from "./Components/LandingMenu.jsx";
import clouds from "../../../assets/clouds.png";

function LandingPage() {
    return (
        <>
            {/* Fonte Poppins global para harmonizar */}
            <link
                href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap"
                rel="stylesheet"
            />
            <style>{`
        html, body {
          margin: 0; padding: 0; height: 100%; overflow-x: hidden;
          font-family: 'Poppins', sans-serif;
          background-color: #a1c8ff;
        }
        @keyframes fadeInContent {
          from { opacity: 0; transform: translateY(20px); }
          to { opacity: 1; transform: translateY(0); }
        }
      `}</style>

            <div
                style={{
                    position: "relative",
                    height: "100vh",
                    width: "100vw",
                    backgroundImage: `url(${clouds})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    backgroundRepeat: "no-repeat",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    padding: "1rem",
                    overflowY: "hidden",
                }}
            >
                {/* Mesma overlay para escurecer o fundo */}
                <div
                    style={{
                        position: "absolute",
                        inset: 0,
                        backgroundColor: "rgba(110,110,110,0.25)",
                        zIndex: 0,
                    }}
                />

                {/* Card container */}
                <div
                    style={{
                        position: "relative",
                        zIndex: 1,
                        backgroundColor: "rgba(243, 243, 243, 0.95)", // mesma cor card
                        borderRadius: "2rem",
                        padding: "2rem 2.5rem",
                        maxWidth: "480px",
                        width: "90%",
                        boxSizing: "border-box",
                        boxShadow: "0 20px 40px rgba(0,0,0,0.15)",
                        textAlign: "center",
                        animation: "fadeInContent 1s ease forwards",
                    }}
                >
                    <LandingLogo />

                    <div style={{ marginTop: "2.5rem" }}>
                        <LandingMenu />
                    </div>
                </div>
            </div>
        </>
    );
}

export default LandingPage;
