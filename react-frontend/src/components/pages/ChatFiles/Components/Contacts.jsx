import React, { useEffect, useState } from "react"
import Cookies from "js-cookie"

const Contacts = ({ onSelectContact }) => {
    const [contacts, setContacts] = useState([])
    const [currentUsername, setCurrentUsername] = useState(null)

    useEffect(() => {
        // Recupera o usuário atual do cookie
        const session = Cookies.get("user_session")
        if (session) {
            try {
                const parsed = JSON.parse(session)
                setCurrentUsername(parsed.username)
            } catch (e) {
                console.error("Erro ao parsear user_session:", e)
            }
        }
    }, [])

    useEffect(() => {
        if (!currentUsername) return

        fetch("http://localhost:8080/api/all-users")
            .then(res => res.json())
            .then(usernames => {
                const filtered = usernames
                    .filter(username => username !== currentUsername)
                    .map(username => ({ username }))
                setContacts(filtered)
            })
            .catch(err => console.error("Erro ao buscar contatos:", err))
    }, [currentUsername])

    return (
        <div className="p-3">
            <h5>Contatos</h5>
            <ul className="list-group">
                {contacts.map((contact, idx) => (
                    <li
                        key={idx}
                        className="list-group-item list-group-item-action"
                        onClick={() => onSelectContact(contact)}
                        style={{ cursor: "pointer" }}
                    >
                        {contact.username}
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default Contacts
