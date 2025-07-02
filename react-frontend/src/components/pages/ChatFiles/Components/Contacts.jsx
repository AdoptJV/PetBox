import React, { useEffect, useState } from "react"
import Cookies from "js-cookie"
import userIcon from "../../../../assets/person-circle.svg" // ajuste o caminho se necessário

const Contacts = ({ onSelectContact }) => {
    const [contacts, setContacts] = useState([])
    const [currentUsername, setCurrentUsername] = useState(null)

    useEffect(() => {
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
                        className="list-group-item list-group-item-action d-flex align-items-center"
                        onClick={() => onSelectContact(contact)}
                        style={{ cursor: "pointer" }}
                    >
                        <img
                            src={`http://localhost:8080/api/pfps/${contact.username}_pfp.jpg`}
                            onError={(e) => {
                                e.currentTarget.src = userIcon
                            }}
                            alt="PFP"
                            className="rounded-circle me-2"
                            style={{ width: "40px", height: "40px", objectFit: "cover" }}
                        />
                        <span>{contact.username}</span>
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default Contacts
