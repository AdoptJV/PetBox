import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LandingPage from "./components/pages/LandingPageFiles/LandingPage.jsx"
import HomePage from "./components/pages/HomePageFiles/HomePage.jsx";
import RegisterPetPage from "./components/pages/RegisterPetFiles/RegisterPetPage.jsx";
import RegisterUserPage from "./components/pages/RegisterUserFiles/RegisterUserPage.jsx";
import ChatPage from "./components/pages/ChatFiles/ChatPage.jsx";
import ProtectedRoute from "./ProtectedRoutes.jsx";
import PostPage from "./components/pages/PostFiles/PostPage.jsx";
import ProfilePage from "./components/pages/ProfileFiles/ProfilePage.jsx";
import PetPage from "./components/pages/PetFiles/PetPage.jsx";
import SearchPage from "./components/pages/SearchFiles/SearchPage.jsx";
import ONGPage from "./components/pages/ONGFiles/ONGPage.jsx";

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

                <Route path="/write" element={
                    <ProtectedRoute>
                        <PostPage/>
                    </ProtectedRoute>
                }/>

                <Route path="/profile/:userId" element={
                    <ProtectedRoute>
                        <ProfilePage/>
                    </ProtectedRoute>
                }/>

                <Route path="/pets/:petId" element={
                    <ProtectedRoute>
                        <PetPage/>
                    </ProtectedRoute>
                }/>

                <Route path="/ongs" element={
                    <ProtectedRoute>
                        <ONGPage/>
                    </ProtectedRoute>
                }
                />

                <Route path="/settings" element={
                    <ProtectedRoute>
                        <ONGPage/>
                    </ProtectedRoute>
                }
                />

                <Route path="/search" element={
                    <ProtectedRoute>
                        <SearchPage/>
                    </ProtectedRoute>
                }/>

            </Routes>
        </Router>
    );
}

export default App;