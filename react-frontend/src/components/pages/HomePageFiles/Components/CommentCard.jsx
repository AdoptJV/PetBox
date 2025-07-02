import Comment from "./Comment.jsx";
import chatBubble from "../../../../assets/chat-heart.svg";
import React, { useEffect, useState } from "react";

function CommentCard({ postID }) {
    const [comments, setComments] = useState([]);

    async function fetchComments() {
        try {
            const res = await fetch("http://localhost:8080/api/comments", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ postID }),
            });

            if (!res.ok) {
                console.error("Server error", res.status);
                return;
            }

            const data = await res.json();
            setComments(data);
        } catch (e) {
            console.error("Fetch failed:", e);
        }
    }

    useEffect(() => {
        fetchComments();
    }, []);

    const [commentData, setCommentData] = useState("");
    const [response, setResponse] = useState("");

    const handleChange = (e) => {
        setCommentData(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!commentData.trim()) return;

        try {
            const res = await fetch("http://localhost:8080/api/writecomment", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({
                    comment: commentData,
                    post: postID,
                }),
            });
            if (res.ok) {
                setCommentData("");
                fetchComments();
            } else {
                const data = await res.json();
                setResponse(`Error: ${data.message || "Server error"}`);
            }
        } catch (error) {
            setResponse("Failed to connect to server");
        }
    };

    // Aqui o onKeyDown para enviar no Enter
    const handleKeyDown = (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault(); // Evita pular linha
            handleSubmit(e);
        }
    };

    return (
        <div className="card overflow-y-scroll" style={{ width: "20rem", height: "100%" }}>
            <div className="card-body">
                <h6>COMENTÁRIOS</h6>
                <form onSubmit={handleSubmit} className="d-flex mb-3" style={{ width: "100%" }}>
          <textarea
              className="form-control"
              rows="1"
              value={commentData}
              onChange={handleChange}
              onKeyDown={handleKeyDown} // <-- aqui
              placeholder="O que quer falar?"
              style={{
                  width: "85%",
                  borderTopRightRadius: 0,
                  borderBottomRightRadius: 0,
              }}
          />
                    <button
                        type="submit"
                        className="btn btn-primary d-flex justify-content-center align-items-center p-2"
                        disabled={commentData.length === 0}
                        style={{
                            width: "15%",
                            borderTopLeftRadius: 0,
                            borderBottomLeftRadius: 0,
                            whiteSpace: "nowrap",
                        }}
                    >
                        <img
                            src={chatBubble}
                            alt="Comentário"
                            style={{ width: "20px", height: "20px", pointerEvents: "none" }}
                        />
                    </button>
                </form>

                <ul style={{ listStyle: "none", padding: 0, margin: 0, flexGrow: 1, minHeight: 0 }}>
                    {(!comments || comments.length === 0) ? (
                        <li className="text-center">Aguardando comentários...</li>
                    ) : (
                        comments.map((comment, idx) => (
                            <li className="border-bottom py-1" key={comment.id}>
                                <Comment comment={comment} />
                            </li>
                        ))
                    )}
                </ul>

                {response && (
                    <div className={`alert ${response.includes("Error") ? "alert-danger" : "alert-success"} mt-3`}>
                        {response}
                    </div>
                )}
            </div>
        </div>
    );
}

export default CommentCard;
