function PetAgeRange({ minAge, maxAge, onMinChange, onMaxChange }) {
    const handleNumericInput = (e, callback) => {
        const value = e.target.value;
        if (value === '' || /^[0-9\b]+$/.test(value)) {
            const newEvent = {
                ...e,
                target: {
                    ...e.target,
                    value: value === '' ? '' : parseInt(value, 10) || ''
                }
            };
            callback(newEvent);
        }
    };

    return (
        <div className="col-md-2 mx-2 my-2">
            <label className="form-label">Faixa de idade do PET:</label>
            <div className="d-flex align-items-center gap-3">
                <div className="flex-grow-1">
                    <input
                        name="minAge"
                        className="form-control rounded-4"
                        type="text"
                        min="0"
                        max={maxAge !== "" ? maxAge : undefined}
                        value={minAge}
                        onChange={(e) => handleNumericInput(e, onMinChange)}
                        placeholder="Mín"
                        inputMode="numeric"
                    />
                </div>
                <div className="flex-grow-1">
                    <input
                        name="maxAge"
                        className="form-control rounded-4"
                        type="text"
                        min={minAge !== "" ? minAge : undefined}
                        value={maxAge}
                        onChange={(e) => handleNumericInput(e, onMaxChange)}
                        placeholder="Máx"
                        inputMode="numeric"
                    />
                </div>
            </div>
        </div>
    );
}

export default PetAgeRange