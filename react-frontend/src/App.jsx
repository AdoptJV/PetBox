import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LandingPage from "./components/pages/LandingPageFiles/LandingPage.jsx"
import HomePage from "./components/pages/HomePageFiles/HomePage.jsx";
import RegisterPetPage from "./components/pages/RegisterPetFiles/RegisterPetPage.jsx";
import RegisterUserPage from "./components/pages/RegisterUserFiles/RegisterUserPage.jsx";
import ChatPage from "./components/pages/ChatFiles/ChatPage.jsx";
import ProtectedRoute from "./ProtectedRoutes.jsx";

function App() {

    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage/>}/>
                <Route path="/register" element={<RegisterUserPage/>}/>
                <Route path="/home" element={
                    <ProtectedRoute>
                    <HomePage/>
                    </ProtectedRoute>
                }/>
                <Route path="/pet-register" element={
                    <ProtectedRoute>
                    <RegisterPetPage/>
                    </ProtectedRoute>
                }/>

                <Route path="/chat" element={
                    <ProtectedRoute>
                    <ChatPage/>
                    </ProtectedRoute>
                }/>
         </Routes>
        </Router>
    );
}

export default App;