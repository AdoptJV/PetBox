import Bars from "../../general/Bars.jsx";
import HorizontalList from "../../general/HorizontalList.jsx";
import FeedScroll from "./Components/FeedScroll.jsx";
import WritePost from "../../general/WritePost.jsx";

function HomePage() {

    return (
        <div style={{backgroundColor: "#f3f3f3", minHeight: "100vh"}}>
            <Bars>
                <h4 className="mx-3 my-3 text-center">PETs próximos a você!</h4>
                <HorizontalList/>
                <hr className="mx-3"/>
                <h5 className="mx-3 my-3 text-center">Posts</h5>
                <FeedScroll/>
            </Bars>
            <WritePost/>
        </div>
    )
}

export default HomePage;