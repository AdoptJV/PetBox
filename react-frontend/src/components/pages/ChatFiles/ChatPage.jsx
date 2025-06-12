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
                if (parsed.username) {
                    setUsername(parsed.username)
                }
            } catch (e) {
                console.error("Erro ao parsear o cookie user_session:", e)
            }
        }
    }, [])

    useEffect(() => {
        if (!username) return

        console.log("Conectando como:", username)
        socket.current = new WebSocket(`ws://localhost:8080/api/chat?username=${encodeURIComponent(username)}`)

        socket.current.onmessage = (event) => {
            const message = event.data
            setMessages(prev => [...prev, message])
        }

        socket.current.onopen = () => { console.log("WebSocket conectado") }
        socket.current.onerror = (error) => { console.error("WebSocket error:", error) }
        socket.current.onclose = () => { console.log("WebSocket desconectado") }

        return () => {
            socket.current?.close()
        }
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
        if (e.key === "Enter") {
            sendMessage()
        }
    }

    if (!username) {
        return <p>Você precisa estar logado para usar o chat.</p>
    }

    return (
        <div>
            <h2>Chat - Olá, {username}</h2>
            <div style={{ height: "300px", overflowY: "auto", border: "1px solid black", padding: "5px" }}>
                {messages.map((msg, idx) => (
                    <div key={idx}>{msg}</div>
                ))}
                <div ref={messagesEndRef} />
            </div>

            <input
                type="text"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={handleKeyPress}
                placeholder="Digite sua mensagem"
            />
            <button onClick={sendMessage}>Enviar</button>
        </div>
    )
}

export default Chat
