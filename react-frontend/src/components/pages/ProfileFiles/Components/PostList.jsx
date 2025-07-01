import FeedCard from "../../HomePageFiles/Components/FeedCard.jsx";
import {useEffect, useState} from "react";

function PostList({username}) {
    const [posts, setPosts] = useState("");
    useEffect(() => {
        fetch("http://localhost:8080/api/profileposts", {
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