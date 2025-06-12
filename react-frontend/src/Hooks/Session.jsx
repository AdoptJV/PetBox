import { useEffect, useState } from "react";

/* verifica se o usuario tem esta logado utilizando cookies e o checker do backend*/

export default function useSession() {
    const [isLoading, setIsLoading] = useState(true);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        fetch("http://localhost:8080/api/check/user", {
            credentials: "include",
        })
            .then((res) => {
                if (res.ok) {
                    setIsLoggedIn(true);
                } else {
                    setIsLoggedIn(false);
                }
            })
            .catch(() => setIsLoggedIn(false))
            .finally(() => setIsLoading(false));
    }, []);

    return { isLoading, isLoggedIn };
}
