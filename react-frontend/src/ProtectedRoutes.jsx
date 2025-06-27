import {Navigate} from 'react-router-dom';
import useSession from "./Hooks/Session.jsx";

function ProtectedRoute({children}) {
    const {isLoading, isLoggedIn} = useSession();

    if (isLoading) return <div>Carregando...</div>;

    if (!isLoggedIn) {
        return <Navigate to='/'/>;
    }

    return children;
}

export default ProtectedRoute;