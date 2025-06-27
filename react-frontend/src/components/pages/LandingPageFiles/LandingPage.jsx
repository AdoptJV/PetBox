import LandingLogo from "./Components/LandingLogo.jsx";
import LandingMenu from "./Components/LandingMenu.jsx";
import clouds from "../../../assets/clouds.png";

/* pagina inicial*/

function LandingPage() {
    return (
        <div style={{
            backgroundImage: `url(${clouds})`,
            backgroundColor: "#a1c8ff",
            minHeight: "100vh",
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat"
        }}>
            <div className="container text-center">
                <div className="row align-items-center" style={{ minHeight: "100vh" }}>
                    <div className="col">
                        <LandingLogo/>
                    </div>
                    <div className="col">
                        <LandingMenu/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LandingPage;