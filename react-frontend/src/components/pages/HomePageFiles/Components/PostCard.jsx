import userIcon from "../../../../assets/person-circle.svg";
import {useEffect, useState} from "react";

function PostCard({ postData }) {
    const [response, setResponse] = useState(null);
    const userId = postData.ownerID;

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const res = await fetch("http://localhost:8080/api/poster", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ userId: userId }),
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
        <div className="card" style={{width: "40rem", maxWidth: "100%"}}>
            <div className="card-header bg-white py-3">
                <div className="d-flex align-items-center">
                    <a href="/profile">
                        <img src={`http://localhost:8080/api/pfps/${response ? response.username : "Loading..."}_pfp.jpg`}
                             onError={(e) => {
                                 e.currentTarget.src = userIcon;
                                 e.currentTarget.className = "mx-3 d-inline-block align-top rounded-circle shadow";
                             }}
                             width="35" height="35"
                             className="me-3 rounded-circle shadow" alt="User profile" />
                    </a>
                    <a className="text-decoration-none text-reset" href="/profile">
                        <h5 className="m-0">{response ? response.username : "Loading..."}</h5>
                    </a>
                </div>
            </div>

            <div className="overflow-hidden" style={{height: "30rem"}}>
                <img
                    src={postData.imgUrl}
                    className="w-100 h-100 object-fit-cover"
                    alt="Post content"
                />
            </div>

            <div className="card-body py-3" style={{ paddingBottom: 0 }}>
            <p className="card-text mb-0">
                {postData.caption}
                </p>
            </div>
        </div>
    );
}

export default PostCard;