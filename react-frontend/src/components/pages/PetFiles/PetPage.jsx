import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Bars from '../../general/Bars.jsx';
import userIcon from '../../../assets/person-circle.svg';

function PetPage() {
    const { petId } = useParams();
    const [pet, setPet] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        async function fetchPet() {
            try {
                const res = await fetch(`http://localhost:8080/api/pets/${petId}`);
                if (!res.ok) throw new Error(`Status ${res.status}`);
                const data = await res.json();
                console.log("recebeu", data);
                setPet(data);
            } catch (e) {
                setError('Não foi possível carregar os dados do pet.');
                console.error(e);
            }
        }
        fetchPet();
    }, [petId]);

    if (error) return <div className="alert alert-danger text-center my-5">{error}</div>;
    if (!pet) return <div className="text-center my-5">Carregando...</div>;

    return (
        <Bars>
            <div className="container d-flex justify-content-center align-items-center my-5">
                <div className="card p-4 shadow rounded-4" style={{ maxWidth: '600px', width: '100%' }}>
                    <div className="text-center">
                        <img
                            src={`http://localhost:8080/api/petimg/${petId}_pfp.jpg`}
                            onError={(e) => { e.currentTarget.src = userIcon }}
                            className="rounded-circle shadow mb-3"
                            alt="Foto do pet"
                            style={{ width: '180px', height: '180px', objectFit: 'cover' }}
                        />
                        <h2 className="fw-bold">{pet.name}</h2>
                        <div className="text-center mt-2">
                            <span className="text-muted">Dono: </span>
                            <Link
                                to={`/profile/${pet.ownerID}`}
                                className="fw-semibold text-decoration-none"
                            >
                                {pet.owner}
                            </Link>
                        </div>
                    </div>

                    <hr />

                    <ul className="list-group list-group-flush">
                        <li className="list-group-item d-flex justify-content-between">
                            <strong>Espécie:</strong>
                            <span>{pet.species}</span>
                        </li>
                        <li className="list-group-item d-flex justify-content-between">
                            <strong>Idade:</strong>
                            <span>{pet.age} anos</span>
                        </li>
                        <li className="list-group-item d-flex justify-content-between">
                            <strong>Castrado:</strong>
                            <span>{pet.castrated ? 'Sim' : 'Não'}</span>
                        </li>
                    </ul>

                    <div className="mt-4">
                        <h5 className="mb-2">Descrição</h5>
                        <p className="text-muted" style={{ whiteSpace: 'pre-wrap' }}>
                            {pet.description || '—'}
                        </p>
                    </div>
                </div>
            </div>
        </Bars>
    );
}

export default PetPage;
