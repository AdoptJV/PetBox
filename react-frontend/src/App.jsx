import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LandingPage from "./components/pages/LandingPage.jsx"
import HomePage from "./components/pages/HomePage.jsx";
import RegisterPetPage from "./components/pages/RegisterPetPage.jsx";
import RegisterUserPage from "./components/pages/RegisterUserFiles/RegisterUserPage.jsx";
import Cep from "./components/pages/RegisterUserFiles/Components/Cep.jsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage/>}/>
                <Route path="/register" element={<RegisterUserPage/>}/>
                <Route path="/cep" element={<Cep/>} />
                <Route path="/home" element={<HomePage/>}/>
                <Route path="/petregister" element={<RegisterPetPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;