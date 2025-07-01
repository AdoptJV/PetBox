import Bars from "../../general/Bars.jsx";
import PetList from "./Components/PetList.jsx";
import PostList from "./Components/PostList.jsx";
import WritePost from "../../general/WritePost.jsx";
import {useEffect, useState} from "react";
import userIcon from "../../../assets/person-circle.svg";

function ProfilePage() {
    const [username, setUsername] = useState("");
    useEffect(() => {
        fetch("http://localhost:8080/api/nav", {
            credentials: "include",
        })
            .then(res => res.json())
            .then(data => setUsername(data.username))
            .catch(() => setUsername(""));
    }, []);

    return (
        <div style={{backgroundColor: "#f3f3f3", minHeight: "100vh"}}>
            <Bars>
                <div className="text-center my-3">
                    <img src={`http://localhost:8080/api/pfps/${username}_pfp.jpg`}
                         onError={(e) => {
                             e.currentTarget.src = userIcon;
                             e.currentTarget.className = "mx-3 d-inline-block align-top rounded-circle shadow";
                         }}
                         width="200" height="200" className="mx-3 shadow d-inline-block align-top rounded-circle"
                         alt="Foto do usuário"/>
                    <h1 className="my-3">{username}</h1>
                </div>
                <hr className="mx-3"/>
                <h4 className="mx-3 my-3 text-center">PETs de {username}</h4>
                <PetList username={username}/>
                <hr className="mx-3"/>
                <h5 className="mx-3 my-3 text-center">Posts de {username}</h5>
                <PostList username={username}/>
            </Bars>
            <WritePost/>
        </div>
    )
}

export default ProfilePage;