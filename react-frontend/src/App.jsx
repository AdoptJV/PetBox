import React, {useEffect, useState} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LandingPage from "./components/pages/LandingPageFiles/LandingPage.jsx"
import HomePage from "./components/pages/HomePageFiles/HomePage.jsx";
import RegisterPetPage from "./components/pages/RegisterPetFiles/RegisterPetPage.jsx";
import RegisterUserPage from "./components/pages/RegisterUserFiles/RegisterUserPage.jsx";

function App() {

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<LandingPage/>}/>
                <Route path="/register" element={<RegisterUserPage/>}/>
                <Route path="/home" element={<HomePage/>}/>
                <Route path="/petregister" element={<RegisterPetPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;