import Comment from "./Comment.jsx";
import {useEffect, useState} from "react";

function CommentCard({postID}) {
    console.log("COMMENT CARD WITH" + postID)
    const [comments, setComments] = useState("");
    useEffect(() => {
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

        fetchUserInfo();
    }, []);

    return (
        <div className="card overflow-y-scroll" style={{ width: "20rem", height: "100%"}}>
            <div className="card-body">
                <h6>COMENTÁRIOS</h6>
                <ul style={{ listStyle: "none", padding: 0, margin: 0, flexGrow: 1, minHeight: 0 }}>
                    {(!comments || comments.length === 0) ? (
                        <li className="text-center">Aguardando comentários...</li>
                    ) : (
                        comments.map((comment, idx) => (
                            <li className="border-bottom py-1" key={idx}>
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