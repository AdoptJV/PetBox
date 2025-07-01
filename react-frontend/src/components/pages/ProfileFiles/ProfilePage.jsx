import Bars from "../../general/Bars.jsx";
import PetList from "./Components/PetList.jsx";
import PostList from "./Components/PostList.jsx";
import WritePost from "../../general/WritePost.jsx";
import {useEffect, useState} from "react";
import userIcon from "../../../assets/person-circle.svg";
import { useParams } from "react-router-dom"

function ProfilePage() {
    const { userId} = useParams()
    console.log("USER ID PP" + userId)
    const [response, setResponse] = useState(null);
    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const res = await fetch("http://localhost:8080/api/getuser", {
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
        <div style={{backgroundColor: "#f3f3f3", minHeight: "100vh"}}>
            <Bars>
                <div className="text-center my-3">
                    <img src={`http://localhost:8080/api/pfps/${response ? response.username : "Loading..."}_pfp.jpg`}
                         onError={(e) => {
                             e.currentTarget.src = userIcon;
                             e.currentTarget.className = "mx-3 d-inline-block align-top rounded-circle shadow";
                         }}
                         width="200" height="200" className="mx-3 shadow d-inline-block align-top rounded-circle"
                         alt="Foto do usuário"/>
                    <h1 className="my-3">{response ? response.username : "Loading..."}</h1>
                </div>
                <hr className="mx-3"/>
                <h4 className="mx-3 my-3 text-center">PETs de {response ? response.username : "Loading..."}</h4>
                <PetList username={response ? response.username : "Loading..."}/>
                <hr className="mx-3"/>
                <h5 className="mx-3 my-3 text-center">Posts de {response ? response.username : "Loading..."}</h5>
                <PostList username={response ? response.username : "Loading..."}/>
            </Bars>
            <WritePost/>
        </div>
    )
}

export default ProfilePage;