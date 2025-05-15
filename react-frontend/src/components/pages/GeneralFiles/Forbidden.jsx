import React from 'react';

const Forbidden = () => {
    return (
        <div style={{ textAlign: 'center', padding: '10% 20%', fontFamily: 'sans-serif' }}>
            <h1 style={{ fontSize: '3rem' }}>403 Forbidden</h1>
            <p>Você não tem permissão para acessar este recurso.</p>
            <hr />
            <p>nginx/1.18.0 (ou Apache)</p> {/* só pra simular um erro de servidor */}
        </div>
    );
};

export default Forbidden;