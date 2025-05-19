import placeholder from "../../../../assets/ph2.jpg";
import userIcon from "../../../../assets/person-circle.svg";

function PostCard() {
    return (
        <div className="card" style={{width: "40rem", maxWidth: "100%"}}>
            <div className="card-header bg-white py-3">
                <div className="d-flex align-items-center">
                    <a href="/profile">
                        <img src={userIcon} width="35" height="35" className="me-3" alt="User profile" />
                    </a>
                    <a className="text-decoration-none text-reset" href="/profile">
                        <h5 className="m-0">Usuário</h5>
                    </a>
                </div>
            </div>

            <div className="overflow-hidden" style={{height: "30rem"}}>
                <img
                    src={placeholder}
                    className="w-100 h-100 object-fit-cover"
                    alt="Post content"
                />
            </div>

            <div className="card-body py-3" style={{ paddingBottom: 0 }}>
            <p className="card-text mb-0">
                    Esse gatinho azul foi abandonado na rua Augusta. Muito carinhoso e precisa de um lar.
                </p>
            </div>
        </div>
    );
}

export default PostCard;