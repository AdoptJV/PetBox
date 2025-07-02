import FeedCard from "../../HomePageFiles/Components/FeedCard.jsx";
import {useEffect, useState} from "react";

function PostList({username}) {
    console.log("USERNAME PL " + username)
    const [posts, setPosts] = useState(null);
    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const res = await fetch("http://localhost:8080/api/profileposts", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username: username }),
                });

                if (!res.ok) {
                    console.error("Server error", res.status);
                    return;
                }

                const data = await res.json();
                console.log("Received data:", data);
                setPosts(data);
            } catch (e) {
                console.error("Fetch failed:", e);
            }
        }

        fetchUserInfo();
    }, [username]);
    console.log(posts)

    return (
        <div className="w-100 text-center">
            <div className="d-flex flex-column flex-nowrap">
                {
                    !posts || posts.length === 0 ? (
                        <p className="text-center">{username} não possui posts :(</p>
                    ) : (
                        posts.map((post, idx) => (
                            <div className="row mx-2 my-3 justify-content-center" key={idx}>
                                <FeedCard postData={post} />
                            </div>
                        ))
                    )
                }
            </div>
        </div>

    );
}

export default PostList