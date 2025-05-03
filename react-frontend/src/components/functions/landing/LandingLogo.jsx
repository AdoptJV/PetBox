import petboxLogo from "../../../assets/bigLogo.svg";

function LandingLogo() {
    return <img src={petboxLogo} alt={"PetBox Logo"} width={"100%"}
    style={{ filter: "drop-shadow(0 2px 4px rgba(0,0,0,0.2))" +
            "drop-shadow(0 4px 8px rgba(0,0,0,0.2)) " +
            "drop-shadow(0 8px 16px rgba(0,0,0,0.2))",
        transform: "translateZ(0)" }}
    />;
}

export default LandingLogo;