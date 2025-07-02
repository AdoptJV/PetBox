import Comment from "./Comment.jsx";
import chatBubble from "../../../../assets/chat-heart.svg";
import React, {useEffect, useState} from "react";

function CommentCard({postID}) {
    const [comments, setComments] = useState("");
    async function fetchUserInfo() {
        try {
            const res = await fetch("http://localhost:8080/api/comments", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ postID: postID }),
            });

            if (!res.ok) {
                console.error("Server error", res.status);
                return;
            }

            const data = await res.json();
            console.log("Received data:", data);
            setComments(data);
        } catch (e) {
            console.error("Fetch failed:", e);
        }
    }

    useEffect(() => {
        fetchUserInfo();
    }, []);

    const [commentData, setCommentData] = useState("")

    const handleChange = (e) => {
        setCommentData(e.target.value);
    };

    const [response, setResponse] = useState("");
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append("comment", commentData);
            formData.append("post", postID);
            console.log("Submitting comment:", commentData, "for post", postID);
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
            if(res.ok) {
                console.log("ok")
                setCommentData("");
                fetchUserInfo();
            } else {
                const data = await res.json();
                setResponse(`Error: ${data.message || "Server error"}`);
            }
        } catch (error) {
            setResponse("Failed to connect to server");
        }
    };

    return (
        <div className="card overflow-y-scroll" style={{ width: "20rem", height: "100%"}}>
            <div className="card-body">
                <h6>COMENTÁRIOS</h6>
                <form onSubmit={handleSubmit} className="d-flex mb-3" style={{ width: "100%" }}>
                    <textarea
                        className="form-control"
                        rows="1"
                        value={commentData}
                        onChange={handleChange}
                        placeholder="O que quer falar?"
                        style={{
                            width: "85%",
                            borderTopRightRadius: 0,
                            borderBottomRightRadius: 0,
                        }}
                    />
                    <button
                        type="submit"
                        className="btn btn-primary"
                        disabled={commentData.length === 0}
                        style={{
                            width: "15%",
                            borderTopLeftRadius: 0,
                            borderBottomLeftRadius: 0,
                            whiteSpace: "nowrap",
                        }}
                    >
                        <img src={chatBubble} alt="Comentário" className="mx-0" />
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
            </div>
        </div>
    )
}

export default CommentCard