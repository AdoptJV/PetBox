import Comment from "./Comment.jsx";

function CommentCard() {
    return (
        <div className="card overflow-y-scroll" style={{ width: "20rem", height: "100%"}}>
            <div className="card-body">
                <h6>COMENTÁRIOS</h6>
                <ul style={{listStyle: "none", padding: 0, margin: 0, flexGrow : 1, minHeight: 0}}>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                    <li className="border-bottom py-1"><Comment/></li>
                </ul>
            </div>
        </div>
    )
}

export default CommentCard