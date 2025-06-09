import React, { useEffect, useState } from 'react';

function Species({specie, race, onChange}) {
    const [speciesList, setSpeciesList] = useState([]);
    const [raceList, setRaceList] = useState([]);


    useEffect(() => {
        // Busca o JSON da rota GET no backend
        fetch('http://localhost:8080/api/JSON/species')
            .then((res) => res.json())
            .then((json) => {
                setSpeciesList(json.especie);
            })
            .catch((err) => console.error('Erro ao carregar species.json:', err));
    }, []);

    // Atualiza lista de raças quando muda a espécie selecionada
    useEffect(() => {
        const sp = speciesList.find((s) => s.nome === specie);
        setRaceList(sp ? sp.racas : []);
    }, [specie, speciesList]);

    return (
        <div className="col-md-4 mb-2">
            <div>
                <label htmlFor="species" className="form-label mb-2">
                    Espécie
                </label>
                <select
                    id="species"
                    name="specie"
                    value={specie}
                    onChange={(e) => onChange(e)}
                    className="form-select"
                >
                    <option value="">Selecione uma espécie</option>
                    {speciesList.map((s) => (
                        <option key={s.nome} value={s.nome}>
                            {s.nome}
                        </option>
                    ))}
                </select>
            </div>
            { specie && (
            <div className="col-md-4 ">
                <label htmlFor="race" className="form-label mt-3">
                    Raça
                </label>
                <select
                    id="race"
                    name="race"
                    value={race}
                    onChange={(e) => onChange(e)}
                    className="form-select"
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