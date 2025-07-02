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
        <div className="container d-flex flex-column py-3" style={{ height: "calc(100vh - 65px)" }}>
        <h2 className="mb-3">Chat com {contact.username}</h2>

            <div className="flex-grow-1 overflow-auto border rounded p-3 bg-light" style={{ minHeight: "300px" }}>
                {messages.map((msg, idx) => {
                    const isOwn = msg.from === username
                    const timestampMs = Number(msg.timestamp) < 10000000000 ? Number(msg.timestamp) * 1000 : Number(msg.timestamp)
                    const date = new Date(timestampMs)
                    date.setTime(date.getTime() - 3 * 60 * 60 * 1000)

                    const formattedTime = date.toLocaleTimeString("pt-BR", {
                        hour: "2-digit",
                        minute: "2-digit",
                        hour12: false,
                    })


                    return (
                        <div
                            key={idx}
                            className={`mb-3 d-flex ${isOwn ? "justify-content-end" : "justify-content-start"}`}
                        >
                            <div style={{ maxWidth: "70%" }}>
                                {/* Nome e horário */}
                                <div
                                    style={{
                                        fontSize: "0.75rem",
                                        color: "#666",
                                        marginBottom: "2px",
                                        display: "flex",
                                        justifyContent: isOwn ? "flex-end" : "flex-start",
                                        gap: "6px",
                                        // Aqui o texto no bloco todo será alinhado de acordo com o dono da mensagem:
                                        textAlign: isOwn ? "right" : "left",
                                    }}
                                >
                                    {isOwn ? (
                                        <>
                                            <span>{formattedTime}</span>
                                            <span style={{ fontWeight: "bold" }}>{msg.from}</span>
                                        </>
                                    ) : (
                                        <>
                                            <span style={{ fontWeight: "bold" }}>{msg.from}</span>
                                            <span>{formattedTime}</span>
                                        </>
                                    )}
                                </div>
                                {/* Mensagem */}
                                <div
                                    style={{
                                        backgroundColor: isOwn ? "#0d6efd" : "#6c757d",
                                        color: "white",
                                        padding: "10px 14px",
                                        borderRadius: "1rem",
                                        fontSize: "0.95rem",
                                        whiteSpace: "pre-wrap",
                                        wordBreak: "break-word",
                                    }}
                                >
                                    {msg.content}
                                </div>
                            </div>
                        </div>
                    )
                })}
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
