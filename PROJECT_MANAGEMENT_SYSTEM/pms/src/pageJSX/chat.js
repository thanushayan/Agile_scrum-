import React, { useState, useRef, useEffect } from 'react';
import '../css/chat.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';

const Chat = ({ username, projectId }) => {
    const [isChatOpen, setIsChatOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const messagesRef = useRef(null);

    useEffect(() => {
        fetchChatMessages();
        const intervalId = setInterval(fetchChatMessages, 1000);

        return () => {
            clearInterval(intervalId); // Clean up the interval when the component unmounts
        };
    }, []);

    const fetchChatMessages = async () => {
        const response = await axios.get(`http://localhost:8088/api/chat/${projectId}`);
        setMessages(response.data);
        scrollToBottom();
    };

    const toggleChat = () => {
        setIsChatOpen(!isChatOpen);
    };

    const handleSendMessage = async (message) => {
        console.log(projectId);
        if (message.trim() !== '') {
            const newChatMessage = {
                senderId: username,
                projectId: projectId,
                content: message
            };
            console.log(projectId);

            await axios.post(`http://localhost:8088/api/chat`, newChatMessage);
            setNewMessage('');
            fetchChatMessages();
        }
    };

    const scrollToBottom = () => {
        if (messagesRef.current) {
            const messagesContainer = messagesRef.current;
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }
    };

    return (
        <div className={`chat-container ${isChatOpen ? 'open' : ''}`}>
            {isChatOpen ? (
                <div className="chat-window">
                    <div className="chat-header">
                        <h2>Team Chat</h2>
                        <button onClick={toggleChat}>&times;</button>
                    </div>
                    <div className="chat-messages" ref={messagesRef}>
                        {messages.map((message) => (
                            <div
                                key={message.id}
                                className={`message ${message.senderId === username ? 'sent user1' : 'received user2'}`}
                            >
                                <span>{message.senderId !== username && message.senderId + ':'} </span>
                                <span>{message.content}</span>
                            </div>
                        ))}
                    </div>
                    <form className="chat-form" onSubmit={(e) => { e.preventDefault(); handleSendMessage(newMessage); }}>
                        <div className="send-area">
                            <input
                                type="text"
                                placeholder="Type your message..."
                                value={newMessage}
                                onChange={(e) => setNewMessage(e.target.value)}
                            />
                            <button type="submit" className="button">
                                <FontAwesomeIcon icon={faPaperPlane} />
                            </button>
                        </div>
                    </form>
                </div>
            ) : (
                <div className="chat-toggle-button" onClick={toggleChat}>
                    Chat with Team
                </div>
            )}
        </div>
    );
};

export default Chat;
