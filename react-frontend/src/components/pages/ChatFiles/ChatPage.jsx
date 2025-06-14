import React, { useState } from "react"
import Chat from "./Components/Chat"
import Contacts from "./Components/Contacts"

const ChatPage = () => {
    const [selectedContact, setSelectedContact] = useState(null)

    return (
        <div className="d-flex vh-100">
            <div className="border-end bg-white" style={{ width: "300px" }}>
                <Contacts onSelectContact={setSelectedContact} />
            </div>
            <div className="flex-grow-1">
                <Chat contact={selectedContact} />
            </div>
        </div>
    )
}

export default ChatPage
