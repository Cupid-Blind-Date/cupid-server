import React, {useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ChatRoom = ({ roomId, token }) => {
    const [messages, setMessages] = useState([]);
    const [errors, setErrors] = useState(null);  // 🔴 에러 메시지 상태 추가
    const [message, setMessage] = useState("");
    const clientRef = useRef(null);

    useEffect(() => {
        const socket = new SockJS("http://localhost:8081/ws"); // ✅ SockJS 사용
        const client = new Client({
            webSocketFactory: () => socket, // ✅ WebSocket 대신 SockJS 사용
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            onConnect: () => {
                console.log("✅ WebSocket 연결 성공");
                clientRef.current = client;  // 연결 완료 후 참조 설정

                // ✅ 채팅 메시지 구독
                client.subscribe(`/sub/chat/${roomId}`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    setMessages((prev) => [...prev, receivedMessage]);
                });

                // ✅ 에러 메시지 구독
                client.subscribe("/user/queue/errors", (message) => {
                    const errorMessage = JSON.parse(message.body);
                    console.error("🚨 WebSocket Error:", errorMessage);
                    setErrors(errorMessage.message);
                });
            },
            onStompError: (frame) => {
                console.error("❌ STOMP 에러 발생", frame);
            },
            onWebSocketError: (error) => {
                console.error("❌ WebSocket 연결 오류", error);
            },
            onDisconnect: () => {
                console.log("🔌 WebSocket 연결 해제됨");
            }
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, [roomId, token]);

    const sendMessage = () => {
        if (!clientRef.current || !clientRef.current.connected) {
            console.error("STOMP client is not connected yet.");
            return;
        }

        if (message.trim()) {
            clientRef.current.publish({
                destination: `/pub/chat/${roomId}`,
                body: JSON.stringify({ message }),
            });
            setMessage("");
        }
    };

    return (
        <div>
            <h2>Chat Room {roomId}</h2>

            {/* 🔴 에러 메시지 표시 */}
            {errors && <div style={{ color: "red", marginBottom: "10px" }}>🚨 {errors}</div>}

            <div style={{ border: "1px solid #ccc", padding: "10px", height: "300px", overflowY: "auto" }}>
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.senderId}:</strong> {msg.message}
                    </div>
                ))}
            </div>

            <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder="Type a message..."
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default ChatRoom;
