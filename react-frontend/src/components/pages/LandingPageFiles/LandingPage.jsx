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
          margin: 0; 
          padding: 0; 
          height: 100%; 
          overflow-x: hidden;
          font-family: 'Poppins', sans-serif;
          background-color: #a1c8ff;
          font-size: 16px; /* Base font size for rem units */
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
                {/* Overlay para escurecer o fundo */}
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
                        display: "flex",
                        zIndex: 1,
                        borderRadius: "2rem",
                        padding: "4vh 5vw", // Using viewport units
                        width: "90%", // Percentage of parent
                        maxWidth: "90vw", // Viewport width limit
                        minHeight: "80vh", // Viewport height
                        boxSizing: "border-box",
                        textAlign: "center",
                        animation: "fadeInContent 1s ease forwards",
                        justifyContent: "center",
                        alignItems: "center",
                    }}
                >
                    <div style={{
                        width: "50%",
                        marginBottom: "4vh",
                    }}>
                        <LandingLogo />
                    </div>

                    <div style={{
                        width: "50%",
                    }}>
                        <LandingMenu />
                    </div>
                </div>
            </div>
        </>
    );
}

export default LandingPage;