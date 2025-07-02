import React, { useEffect, useState } from 'react';

function Species({ specie, race, onChange }) {
    const [speciesList, setSpeciesList] = useState([]);
    const [raceList, setRaceList] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/JSON/species')
            .then((res) => res.json())
            .then((json) => {
                setSpeciesList(json.especie);
            })
            .catch((err) => console.error('Erro ao carregar species.json:', err));
    }, []);

    useEffect(() => {
        const sp = speciesList.find((s) => s.nome === specie);
        setRaceList(sp ? sp.racas : []);
    }, [specie, speciesList]);

    return (
        <div className="col-md-5 mx-2 my-2">
            <label htmlFor="species" className="form-label">Espécie</label>
            <select
                id="species"
                name="specie"
                value={specie}
                onChange={onChange}
                className="form-select rounded-4"
            >
                <option value="">Selecione uma espécie</option>
                {speciesList.map((s) => (
                    <option key={s.nome} value={s.nome}>
                        {s.nome}
                    </option>
                ))}
            </select>

            {specie && (
                <div className="mt-3">
                    <label htmlFor="race" className="form-label">Raça</label>
                    <select
                        id="race"
                        name="race"
                        value={race}
                        onChange={onChange}
                        className="form-select rounded-4"
                        disabled={!raceList.length}
                    >
                        <option value="">
                            {raceList.length ? 'Selecione uma raça' : 'Nenhuma raça disponível'}
                        </option>
                        {raceList.map((r, idx) => (
                            <option key={idx} value={r}>
                                {r}
                            </option>
                        ))}
                    </select>
                </div>
            )}
        </div>
    );
}

export default Species;
