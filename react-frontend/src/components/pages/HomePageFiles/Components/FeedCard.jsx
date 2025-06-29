import PostCard from "./PostCard.jsx";
import CommentCard from "./CommentCard.jsx";

function FeedCard({ postData }) {
    return (
        <div className="d-flex justify-content-center" style={{height: "40rem"}}>
            <PostCard postData={postData}/>
            <CommentCard postId={postData.postID}/>
        </div>
    )
}

export default FeedCard