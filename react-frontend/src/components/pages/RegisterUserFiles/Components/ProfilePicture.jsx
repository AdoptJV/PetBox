import React, { useState, useCallback, useRef } from "react";
import Cropper from "react-easy-crop";
import getCroppedImg from "./cropImage"; // Make sure you have this helper

const ProfilePicture = () => {
    const [image, setImage] = useState(null);
    const [fileName, setFileName] = useState("Nenhum arquivo selecionado");
    const [crop, setCrop] = useState({ x: 0, y: 0 });
    const [zoom, setZoom] = useState(1);
    const [croppedAreaPixels, setCroppedAreaPixels] = useState(null);
    const [croppedImage, setCroppedImage] = useState(null);

    const fileInputRef = useRef();

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImage(URL.createObjectURL(file));
            setFileName(file.name);
            setCroppedImage(null);
            setCrop({ x: 0, y: 0 });
            setZoom(1);
            setCroppedAreaPixels(null);
        }
    };

    const onCropComplete = useCallback((_, croppedPixels) => {
        setCroppedAreaPixels(croppedPixels);
    }, []);

    const showCroppedImage = useCallback(async () => {
        try {
            const { blob, previewUrl } = await getCroppedImg(image, croppedAreaPixels);

            setCroppedImage(previewUrl);

            const formData = new FormData();
            formData.append("file", blob, "profile.jpg");

            const response = await fetch("/api/upload", {
                method: "POST",
                body: formData,
            });

            if (!response.ok) throw new Error("Erro no upload");
            const result = await response.json();
            console.log("Resultado do upload:", result);

        } catch (e) {
            console.error(e);
        }
    }, [image, croppedAreaPixels]);

    const reset = () => {
        setImage(null);
        setCroppedImage(null);
        setFileName("Nenhum arquivo selecionado");
        setCrop({ x: 0, y: 0 });
        setZoom(1);
        setCroppedAreaPixels(null);
        fileInputRef.current.value = null;
    };

    return (
        <div className="mx-2 my-2">
            <label>Foto de perfil</label>
            <div className="mb-2 d-flex align-items-center my-2 gap-2">
                <input
                    type="file"
                    accept="image/*"
                    ref={fileInputRef}
                    onChange={handleFileChange}
                    style={{ display: "none" }}
                />
                <button
                    type="button"
                    className="btn btn-primary"
                    onClick={() => fileInputRef.current.click()}
                >
                    Escolher imagem
                </button>
                <span className="text-muted">{fileName}</span>
            </div>

            {image && !croppedImage && (
                <>
                    <div
                        style={{
                            position: "relative",
                            width: 300,
                            height: 300,
                            marginBottom: "1rem",
                        }}
                    >
                        <Cropper
                            image={image}
                            crop={crop}
                            zoom={zoom}
                            aspect={1}
                            onCropChange={setCrop}
                            onZoomChange={setZoom}
                            onCropComplete={onCropComplete}
                        />
                    </div>

                    <div className="d-flex flex-column align-items-center gap-2">
                        <input
                            type="range"
                            min="1"
                            max="3"
                            step="0.1"
                            value={zoom}
                            onChange={(e) =>
                                setZoom(parseFloat(e.target.value))
                            }
                            style={{ width: "200px" }}
                        />

                        <button
                            className="btn btn-primary"
                            onClick={showCroppedImage}
                        >
                            Recortar
                        </button>
                    </div>
                </>
            )}

            {croppedImage && (
                <div className="text-center mt-3">
                    <img
                        src={croppedImage}
                        alt="Cropped"
                        className="rounded-3 shadow"
                        style={{
                            width: 200,
                            height: 200,
                            objectFit: "cover",
                        }}
                    />
                </div>
            )}
        </div>
    );
};

export default ProfilePicture;
