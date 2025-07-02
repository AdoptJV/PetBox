import FeedCard from "./FeedCard.jsx";
import { useEffect, useState } from "react";

function FeedScroll() {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/api/homeposts", {
            credentials: "include",
        })
            .then(res => {
                if (!res.ok) throw new Error("Erro selecionando posts pro feed");
                return res.json();
            })
            .then(data => {
                setPosts(data);
            })
            .catch(error => {
                console.error("Erro selecionando posts pro feed:", error);
                setPosts([]);
            });
    }, []);

    return (
        <div
            style={{
                backgroundColor: "#f7f9fc",
                minHeight: "100vh",
                paddingTop: "3rem",
                paddingBottom: "3rem",
            }}
        >
            <div className="container d-flex flex-column align-items-center px-4">
                {posts.length === 0 ? (
                    <div
                        className="alert alert-info text-center shadow-sm w-100"
                        style={{
                            fontSize: "1.3rem",
                            fontWeight: "500",
                            borderRadius: "1rem",
                            padding: "1.5rem",
                        }}
                    >
                        Não há posts no momento 😢
                    </div>
                ) : (
                    posts.map((post, idx) => (
                        <div
                            key={idx}
                            className="w-100 d-flex justify-content-center mb-5"
                            style={{ maxWidth: "800px" }}
                        >
                            <div
                                className="feed-card-wrapper"
                                style={{
                                    backgroundColor: "white",
                                    borderRadius: "2rem",
                                    boxShadow:
                                        "0 8px 24px rgba(0, 0, 0, 0.08)",
                                    padding: "2rem 2.5rem",
                                    width: "100%",
                                    transition: "transform 0.3s ease, box-shadow 0.3s ease",
                                    cursor: "pointer",
                                }}
                                onMouseEnter={e => {
                                    e.currentTarget.style.transform = "translateY(-8px)";
                                    e.currentTarget.style.boxShadow =
                                        "0 16px 32px rgba(0, 0, 0, 0.14)";
                                }}
                                onMouseLeave={e => {
                                    e.currentTarget.style.transform = "translateY(0)";
                                    e.currentTarget.style.boxShadow =
                                        "0 8px 24px rgba(0, 0, 0, 0.08)";
                                }}
                            >
                                <FeedCard postData={post} />
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export default FeedScroll;
