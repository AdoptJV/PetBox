import React, { useState } from "react"
import Chat from "./Components/Chat"
import Contacts from "./Components/Contacts"
import Bars from "../../general/Bars.jsx"

const ChatPage = () => {
    const [selectedContact, setSelectedContact] = useState(null)

    return (
        <Bars>
            <div className="d-flex" style={{ height: "100%" }}>
                <div className="border-end bg-white" style={{ width: "300px" }}>
                    <Contacts onSelectContact={setSelectedContact} />
                </div>
                <div className="flex-grow-1">
                    <Chat contact={selectedContact} />
                </div>
            </div>
        </Bars>
    )
}

export default ChatPage
