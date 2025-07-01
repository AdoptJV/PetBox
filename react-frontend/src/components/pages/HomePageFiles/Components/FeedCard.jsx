import PostCard from "./PostCard.jsx";
import CommentCard from "./CommentCard.jsx";

function FeedCard({ postData }) {
    console.log(postData)
    return (
        <div className="d-flex justify-content-center" style={{height: "40rem"}}>
            <PostCard postData={postData}/>
            <CommentCard postID={postData.postID}/>
        </div>
    )
}

export default FeedCard