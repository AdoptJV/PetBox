import {Navigate} from 'react-router-dom';
import useSession from "./Hooks/Session.jsx";

function ProtectedRoutes({children}) {
    const {isLoading, isLoggedIn} = useSession();

    if (isLoading) return <p>Loading...</p>;

    if (!isLoggedIn) {
        return <Navigate to='/login'/>;
    }

    return children;
}

export default ProtectedRoutes;