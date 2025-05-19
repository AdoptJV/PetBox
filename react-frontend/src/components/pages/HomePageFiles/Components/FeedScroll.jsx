import FeedCard from "./FeedCard.jsx";

function FeedScroll() {
    return (
        <div className="w-100 text-center">
            <div className="d-flex flex-column flex-nowrap">
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
                <div className="row mx-2 my-3 justify-content-center">
                    <FeedCard/>
                </div>
            </div>
        </div>

    );
}

export default FeedScroll