import React, { useEffect, useRef, useState } from "react"
import Cookies from "js-cookie"

const Chat = ({ contact }) => {
    const [username, setUsername] = useState(null)
    const [messages, setMessages] = useState([])
    const [input, setInput] = useState("")
    const socket = useRef(null)
    const messagesEndRef = useRef(null)
    const contactRef = useRef(null)

    // Guarda o contato atual sempre atualizado
    useEffect(() => {
        contactRef.current = contact
    }, [contact])

    // Pega o username do cookie
    useEffect(() => {
        const session = Cookies.get("user_session")
        if (session) {
            try {
                const parsed = JSON.parse(session)
                if (parsed.username) setUsername(parsed.username)
            } catch (e) {
                console.error("Erro ao parsear o cookie user_session:", e)
            }
        }
    }, [])

    // Carrega histórico
    useEffect(() => {
        if (!username || !contact) return

        fetch(`http://localhost:8080/api/messages?from=${username}&to=${contact.username}`)
            .then(res => res.json())
            .then(data => setMessages(data))
            .catch(err => console.error("Erro ao carregar histórico:", err))
    }, [username, contact])

    // WebSocket para mensagens em tempo realA
    useEffect(() => {
        if (!username || !contact) return

        socket.current = new WebSocket(`ws://localhost:8080/api/chat?username=${encodeURIComponent(username)}`)

        socket.current.onmessage = (event) => {
            const message = JSON.parse(event.data)
            const currentContact = contactRef.current

            if (
                currentContact &&
                (message.from === currentContact.username || message.to === currentContact.username)
            ) {
                setMessages(prev => [...prev, message])
            }
        }

        return () => socket.current?.close()
    }, [username, contact])

    // Scroll automático
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
    }, [messages])

    // Envia mensagem
    const sendMessage = () => {
        if (socket.current && input.trim() !== "") {
            const message = {
                from: username,
                to: contact.username,
                content: input
            }
            socket.current.send(JSON.stringify(message))
            setInput("")
        }
    }

    const handleKeyPress = (e) => {
        if (e.key === "Enter") sendMessage()
    }

    if (!username) {
        return <div className="container mt-5"><p className="alert alert-warning">Você precisa estar logado para usar o chat.</p></div>
    }

    if (!contact) {
        return <div className="container mt-5"><p className="alert alert-info">Selecione um contato para iniciar a conversa.</p></div>
    }

    return (
        <div className="container d-flex flex-column vh-100 py-3">
            <h2 className="mb-3">Chat com {contact.username}</h2>

            <div className="flex-grow-1 overflow-auto border rounded p-3 bg-light" style={{ minHeight: "300px" }}>
                {messages.map((msg, idx) => (
                    <div
                        key={idx}
                        className={`d-flex ${msg.from === username ? "justify-content-end" : "justify-content-start"}`}
                    >
                        <div className={`badge ${msg.from === username ? "bg-primary" : "bg-secondary"}`}>
                            {msg.from}: {msg.content}
                        </div>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>

            <div className="mt-3">
                <div className="input-group">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Digite sua mensagem..."
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyDown={handleKeyPress}
                    />
                    <button className="btn btn-primary" onClick={sendMessage}>Enviar</button>
                </div>
            </div>
        </div>
    )
}

export default Chat
