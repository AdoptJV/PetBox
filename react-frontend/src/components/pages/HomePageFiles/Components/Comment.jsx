import userIcon from "../../../../assets/person-circle.svg";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

function Comment({comment}) {
    const [response, setResponse] = useState(null)
    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const res = await fetch("http://localhost:8080/api/getuser", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ userId: comment.user }),
                });

                if (!res.ok) {
                    console.error("Server error", res.status);
                    return;
                }

                const data = await res.json();
                console.log("Received data:", data);
                setResponse(data);
            } catch (e) {
                console.error("Fetch failed:", e);
            }
        }

        fetchUserInfo();
    }, []);

    return (
        <div className="my-2 d-flex flex-row">
            <Link to={`/profile/${comment.user}`} className="justify-content-center mx-3">
                <img src={`http://localhost:8080/api/pfps/${response ? response.username : "Loading..."}_pfp.jpg`}
                     onError={(e) => {
                         e.currentTarget.src = userIcon;
                         e.currentTarget.className = "d-inline-block align-top rounded-circle shadow";
                     }}
                     width="35" height="35" className="shadow d-inline-block align-top rounded-circle"
                     alt="Foto do usuário"/>
            </Link>
            <div className="d-flex flex-column">
                <Link to={`/profile/${comment.user}`} className="text-decoration-none h6 text-start">{response ? response.username : "Loading..."}</Link>
                <div className="text-sm-start" style={{ wordBreak: "break-word", whiteSpace: "pre-wrap" }}>
                    {comment.text}
                </div>
            </div>
        </div>
    )
}

export default Comment