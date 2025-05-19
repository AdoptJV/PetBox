import userIcon from "../../../../assets/person-circle.svg";

function Comment() {
    return (
        <div className="my-2 d-flex flex-row ">
            <a href="/profile" className="justify-content-center mx-3">
                <img src={userIcon} width="35" height="35" alt="Foto do usuário"/>
            </a>
            <div className="d-flex flex-column">
                <a href="/profile" className="text-decoration-none h6 text-start">Usuário</a>
                <div className="text-sm-start">Nossa que post maneiro!!!</div>
            </div>
        </div>
    )
}

export default Comment