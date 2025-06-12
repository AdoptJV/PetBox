import React, { useEffect, useRef, useState } from "react"
import Cookies from "js-cookie"

const Chat = () => {
    const [username, setUsername] = useState(null)
    const [messages, setMessages] = useState([])
    const [input, setInput] = useState("")
    const socket = useRef(null)
    const messagesEndRef = useRef(null)

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

    useEffect(() => {
        if (!username) return

        socket.current = new WebSocket(`ws://localhost:8080/api/chat?username=${encodeURIComponent(username)}`)

        socket.current.onmessage = (event) => {
            const message = event.data
            setMessages(prev => [...prev, message])
        }

        socket.current.onopen = () => console.log("WebSocket conectado")
        socket.current.onerror = (error) => console.error("WebSocket error:", error)
        socket.current.onclose = () => console.log("WebSocket desconectado")

        return () => socket.current?.close()
    }, [username])

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
    }, [messages])

    const sendMessage = () => {
        if (socket.current && input.trim() !== "") {
            socket.current.send(input)
            setInput("")
        }
    }

    const handleKeyPress = (e) => {
        if (e.key === "Enter") sendMessage()
    }

    if (!username) {
        return <div className="container mt-5"><p className="alert alert-warning">Você precisa estar logado para usar o chat.</p></div>
    }

    return (
        <div className="container d-flex flex-column vh-100 py-3">
            <h2 className="mb-3">Chat - Olá, {username}</h2>

            <div className="flex-grow-1 overflow-auto border rounded p-3 bg-light" style={{ minHeight: "300px" }}>
                {messages.map((msg, idx) => (
                    <div key={idx} className="mb-2">
                        <span className="badge bg-secondary">{msg}</span>
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