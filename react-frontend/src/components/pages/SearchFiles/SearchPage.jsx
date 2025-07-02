import Bars from "../../general/Bars.jsx";
import WritePost from "../../general/WritePost.jsx";
import PetAge from "./Components/PetAgeRange.jsx";
import PetSex from "./Components/PetSex.jsx";
import PetSpecie from "./Components/PetSpecie.jsx";
import PetCastrated from "./Components/PetCastrated.jsx";
import React, { useState } from "react";
import PetList from "./Components/PetList.jsx";

function SearchPage() {
    const [formData, setFormData] = useState({
        minAge: "",
        maxAge: "",
        sex: "",
        specie: "",
        castrated: "",
    });

    const [submittedData, setSubmittedData] = useState(null);
    const [response, setResponse] = useState("");

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleMinAgeChange = (e) => {
        setFormData(prev => ({...prev, minAge: e.target.value}));
    };

    const handleMaxAgeChange = (e) => {
        setFormData(prev => ({...prev, maxAge: e.target.value}));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const normalizedData = {};
        for (const key in formData) {
            normalizedData[key] = formData[key] === "" ? null : formData[key];
        }

        console.log("Filter submitted:", formData);
        setSubmittedData(normalizedData);
        setResponse("");
    };

    return (
        <div className="pb-3" style={{ backgroundColor: "#f3f3f3", minHeight: "100vh" }}>
            <Bars>
                <h4 className="mx-3 my-3 text-center">Filtros</h4>

                <form className="container" onSubmit={handleSubmit}>
                    <div className="d-flex flex-row justify-content-center flex-wrap">
                        <PetAge
                            minAge={formData.minAge}
                            maxAge={formData.maxAge}
                            onMinChange={handleMinAgeChange}
                            onMaxChange={handleMaxAgeChange}
                        />
                        <PetSex value={formData.sex} onChange={handleChange} />
                        <PetSpecie value={formData.specie} onChange={handleChange} />
                        <PetCastrated value={formData.castrated} onChange={handleChange} />
                    </div>

                    <div className="my-3 d-flex justify-content-center">
                        <button type="submit" className="btn btn-primary">Buscar</button>
                    </div>

                    {response && <div className="alert alert-info mt-3">{response}</div>}
                </form>

                <hr className="mx-3" />
                <h5 className="mx-3 my-3 text-center">PETs</h5>
                <PetList formData={submittedData} />
            </Bars>
            <WritePost />
        </div>
    );
}

export default SearchPage;
