import Bars from "../../general/Bars.jsx";
import clouds from "../../../assets/clouds.png";
import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";

function PostPage() {
    const [postData, setPostData] = useState({
        caption: "",
        image: null
    });
    const [previewUrl, setPreviewUrl] = useState(null);
    const [response, setResponse] = useState("");
    const fileInputRef = useRef(null);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPostData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setPostData(prev => ({ ...prev, image: file }));

            // Create preview URL
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewUrl(reader.result);
            };
            reader.readAsDataURL(file);
        }
    };

    const triggerFileInput = () => {
        fileInputRef.current.click();
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append("caption", postData.caption);
            if (postData.image) {
                formData.append("image", postData.image);
            }

            const res = await fetch("http://localhost:8080/api/write", {
                method: "POST",
                body: formData,
                credentials: "include"
            });

            if (res.ok) {
                navigate("/home");
            } else {
                const data = await res.json();
                setResponse(`Error: ${data.message || "Server error"}`);
            }
        } catch (error) {
            setResponse("Failed to connect to server");
        }
    };

    return (
        <Bars>
        <div className="d-flex justify-content-center" style={{
            minHeight: "100vh - 65px",
            backgroundImage: `url(${clouds})`,
            backgroundColor: "#a1c8ff",
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat",
            padding: "15px"
        }}>
            <div className="my-5 py-3 w-50 card shadow rounded-3">
                <h2 className="text-center mb-4">Poste sobre seu PET!</h2>
                <form onSubmit={handleSubmit} className="container">
                    <div className="mb-3 text-center">
                        {previewUrl ? (
                            <div className="position-relative">
                                <img
                                    src={previewUrl}
                                    alt="Preview"
                                    className="img-fluid rounded"
                                    style={{ maxHeight: "300px", cursor: "pointer" }}
                                    onClick={triggerFileInput}
                                />
                                <button
                                    type="button"
                                    className="btn btn-sm btn-danger position-absolute top-0 end-0 m-2"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setPreviewUrl(null);
                                        setPostData(prev => ({ ...prev, image: null }));
                                        fileInputRef.current.value = "";
                                    }}
                                >
                                    ×
                                </button>
                            </div>
                        ) : (
                            <div
                                className="border rounded p-5 text-center bg-light"
                                style={{ cursor: "pointer" }}
                                onClick={triggerFileInput}
                            >
                                <i className="bi bi-image fs-1 text-muted"></i>
                                <p className="mt-2">Click to upload an image</p>
                            </div>
                        )}
                        <input
                            type="file"
                            ref={fileInputRef}
                            className="d-none"
                            accept="image/*"
                            onChange={handleImageChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="caption" className="form-label">Legenda</label>
                        <textarea
                            className="form-control"
                            id="caption"
                            name="caption"
                            rows="3"
                            value={postData.caption}
                            onChange={handleChange}
                            placeholder="O que quer falar?"
                        />
                    </div>

                    <div className="d-grid gap-2">
                        <button
                            type="submit"
                            className="btn btn-primary"
                            disabled={!postData.image} // Disable if no image selected
                        >
                            Post
                        </button>
                    </div>

                    {response && (
                        <div className={`alert ${response.includes("Error") ? "alert-danger" : "alert-success"} mt-3`}>
                            {response}
                        </div>
                    )}
                </form>
            </div>
        </div>
        </Bars>
    );
}

export default PostPage;