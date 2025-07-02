import Bars from "../../general/Bars.jsx";
import HorizontalList from "../../general/HorizontalList.jsx";
import FeedScroll from "./Components/FeedScroll.jsx";
import WritePost from "../../general/WritePost.jsx";

function HomePage() {
    return (
        <div style={{ backgroundColor: "#f3f3f3", minHeight: "100vh" }}>
            <Bars>
                <div className="text-center my-4">
                    <h2 className="fw-bold text-primary">🐾 PETs próximos a você!</h2>
                    <p className="text-muted">Veja os pets disponíveis e faça novos amigos</p>
                </div>

                <HorizontalList />

                <hr className="mx-5 my-4 border-secondary" />

                <div className="text-center my-4">
                    <h3 className="fw-semibold text-success">📢 Últimos posts da comunidade</h3>
                    <p className="text-muted">Compartilhe momentos com seu pet ou veja o que os outros estão postando</p>
                </div>

                <FeedScroll />
            </Bars>

            <WritePost />
        </div>
    );
}

export default HomePage;
