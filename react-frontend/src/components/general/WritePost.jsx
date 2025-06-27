import postIcon from "../../assets/envelope-open-heart.svg";
import { useState } from "react";

function WritePost() {
    const [hovered, setHovered] = useState(false);

    return (
        <div className="position-relative">
            <a
                onMouseEnter={() => setHovered(true)}
                onMouseLeave={() => setHovered(false)}
                href="/write"
                className="position-fixed d-flex justify-content-center align-items-center rounded-circle"
                style={{
                    backgroundColor: "#99c1f1",
                    width: "4rem",
                    height: "4rem",
                    bottom: "2rem",
                    right: "2rem",
                    zIndex: 1000,
                    transform: hovered ? "scale(1.04)" : "scale(1)",
                    transition: "all 0.3s ease",
                    cursor: "pointer",
                    boxShadow: "0 4px 8px rgba(0,0,0,0.1)"
                }}
            >
                <img
                    src={postIcon}
                    width="35"
                    height="35"
                    alt="Faça um post"
                    className="filter-white"
                />
            </a>

            {hovered && (
                <div
                    className="position-fixed bg-dark text-white rounded px-2 py-1"
                    style={{
                        bottom: "6rem",
                        right: "2rem",
                        zIndex: 1001,
                        whiteSpace: "nowrap",
                        animation: "fadeIn 0.3s ease",
                        fontSize: "0.9rem"
                    }}
                >
                    Faça um post
                    <div
                        className="position-absolute bg-dark"
                        style={{
                            width: "10px",
                            height: "10px",
                            bottom: "-5px",
                            right: "1rem",
                            transform: "rotate(45deg)"
                        }}
                    ></div>
                </div>
            )}
        </div>
    );
}

export default WritePost;