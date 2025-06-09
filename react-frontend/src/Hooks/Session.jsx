import { useEffect, useState } from "react";

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
