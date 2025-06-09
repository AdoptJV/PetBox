import React from "react";

const UseTerms = () => {
    return (
        <div className="accordion accordion-flush mb-3 border border-2 border-secondary rounded-1" id="accordionFlushExample">
            <div className="accordion-item">
                <h2 className="accordion-header">
                    <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
                        Termos de uso
                    </button>
                </h2>
                <div id="flush-collapseOne" className="accordion-collapse collapse"
                     data-bs-parent="#accordionFlushExample">
                    <div className="accordion-body">
                        <p>
                            Ao utilizar este serviço, você concorda com os seguintes termos e condições.
                            Reservamo-nos o direito de alterar estes termos a qualquer momento, sem aviso prévio.
                        </p>
                        <p>
                            Você se compromete a utilizar a plataforma de forma ética e legal, não praticando atividades
                            que possam comprometer sua integridade ou a de terceiros.
                        </p>
                        <p>
                            O uso inadequado da plataforma pode resultar na suspensão ou remoção da sua conta.
                            Não nos responsabilizamos por perdas de dados, interrupções de serviço ou falhas causadas
                            por terceiros.
                        </p>
                        <p>
                            Para mais informações, entre em contato com nosso suporte.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default UseTerms;
