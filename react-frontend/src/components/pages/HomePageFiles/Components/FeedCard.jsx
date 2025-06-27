import PostCard from "./PostCard.jsx";
import CommentCard from "./CommentCard.jsx";

function FeedCard() {
    return (
        <div className="d-flex justify-content-center" style={{height: "40rem"}}>
            <PostCard/>
            <CommentCard/>
        </div>
    )
}

export default FeedCard