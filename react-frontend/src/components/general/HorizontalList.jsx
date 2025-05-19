import PetCard from "./PetCard.jsx";

function HorizontalList() {
    return (
        <div className="container-fluid overflow-x-scroll">
            <div className="row flex-row flex-nowrap">
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
                <div className="col mx-1 my-1">
                    <PetCard/>
                </div>
            </div>
        </div>

    );
}

export default HorizontalList