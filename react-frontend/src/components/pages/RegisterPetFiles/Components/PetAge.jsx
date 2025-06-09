function PetAge({value, onChange}) {
    return (
        <div className="row mb-3 col-md-3">
        <label className="form-label">Idade do pet:</label>
        <input
            className="form-control form-control-sm "
            name="age"
            value={value}
            type="number"
            min="0"
            onChange={onChange}
        />
        </div>
    );
}

export default PetAge;