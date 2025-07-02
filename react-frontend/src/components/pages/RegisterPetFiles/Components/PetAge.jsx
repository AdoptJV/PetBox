function PetAge({ value, onChange }) {
    return (
        <div className="col-md-5 mx-2 my-2">
            <label className="form-label">Idade do pet:</label>
            <input
                className="form-control rounded-4"
                name="age"
                value={value}
                type="number"
                min="0"
                placeholder="Digite a idade do pet"
                onChange={onChange}
                required
            />
        </div>
    );
}

export default PetAge;