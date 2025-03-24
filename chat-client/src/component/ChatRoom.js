import React, {useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ChatRoom = ({ roomId }) => {
    const [messages, setMessages] = useState([]);
    const [errors, setErrors] = useState(null);
    const [message, setMessage] = useState("");
    const clientRef = useRef(null);
    const token = localStorage.getItem("accessToken");
    const myId = Number(localStorage.getItem("memberId"));

    useEffect(() => {
        const fetchMessagesAndMarkAsRead = async () => {
            try {
                const response = await fetch(
                    `http://localhost:8081/chat/${roomId}/messages?size=50`,
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                const data = await response.json();
                const fetchedMessages = data.content || [];
                setMessages(fetchedMessages);

                await fetch(`http://localhost:8081/chat/${roomId}/messages/read`, {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                // ✅ 가장 마지막 메시지 ID 추출
                const lastMessage = fetchedMessages[fetchedMessages.length - 1];

                if (lastMessage && clientRef.current && clientRef.current.connected) {
                    clientRef.current.publish({
                        destination: `/pub/read-chat/${roomId}`,
                        body: JSON.stringify({ messageId: lastMessage.id }), // 이 ID는 서버에서 chatMessageId
                    });
                }

            } catch (err) {
                console.error("❌ 초기 메시지 불러오기 실패", err);
            }
        };

        fetchMessagesAndMarkAsRead();
    }, [roomId, token]);

    useEffect(() => {
        const socket = new SockJS("http://localhost:8081/ws");
        const client = new Client({
            webSocketFactory: () => socket,
            connectHeaders: {
                Authorization: `Bearer ${token}`,
                RoomId: `${roomId}`,
            },
            onConnect: () => {
                console.log("✅ WebSocket 연결 성공");
                clientRef.current = client;

                client.subscribe(`/sub/chat/${roomId}`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    setMessages((prev) => [...prev, receivedMessage]);

                    if (receivedMessage.senderId !== myId) {
                        client.publish({
                            destination: `/pub/read-chat/${roomId}`,
                            body: JSON.stringify({ messageId: receivedMessage.chatMessageId }),
                        });
                    }
                });

                client.subscribe(`/sub/read-chat/${roomId}`, (message) => {
                    const { chatMessageId } = JSON.parse(message.body);

                    setMessages((prev) =>
                        prev.map((msg) => {
                            // 내 메시지면서, 해당 메시지 ID 이하이면 읽음 처리
                            alert(msg.id)
                            if (msg.id <= chatMessageId) {
                                return { ...msg, read: true };
                            }
                            return msg;
                        })
                    );
                });

                client.subscribe("/user/queue/errors", (message) => {
                    const errorMessage = JSON.parse(message.body);
                    console.error("🚨 WebSocket Error:", errorMessage);
                    setErrors(errorMessage.message);
                });
            },
            onStompError: (frame) => {
                console.error("❌ STOMP 에러", frame);
            },
            onWebSocketError: (error) => {
                console.error("❌ WebSocket 연결 실패", error);
            },
            onDisconnect: () => {
                console.log("🔌 WebSocket 연결 해제");
            },
        });

        client.activate();
        return () => client.deactivate();
    }, [roomId, token, myId]);

    const sendMessage = () => {
        if (!clientRef.current || !clientRef.current.connected) {
            console.error("STOMP client가 연결되지 않음");
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
        <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
            <h2 style={{ textAlign: "center" }}>Chat Room {roomId}</h2>

            {errors && (
                <div style={{ color: "red", marginBottom: "10px" }}>
                    🚨 {errors}
                </div>
            )}

            <div
                style={{
                    border: "1px solid #ccc",
                    padding: "10px",
                    height: "400px",
                    overflowY: "auto",
                    display: "flex",
                    flexDirection: "column",
                    gap: "10px",
                    backgroundColor: "#d9e5f5",
                }}
            >
                {messages.map((msg, index) => {
                    const isMine = msg.senderId === myId;
                    return (
                        <div
                            key={index}
                            style={{
                                display: "flex",
                                justifyContent: isMine ? "flex-end" : "flex-start",
                            }}
                        >
                            <div style={{ maxWidth: "60%", position: "relative" }}>
                                {!isMine && (
                                    <div style={{ fontSize: "12px", marginBottom: "2px" }}>
                                        👤 {msg.senderId}
                                    </div>
                                )}
                                <div
                                    style={{
                                        backgroundColor: isMine ? "#ffeb3b" : "#ffffff",
                                        padding: "10px",
                                        borderRadius: "10px",
                                        wordBreak: "break-word",
                                        position: "relative",
                                        fontSize: "14px",
                                    }}
                                >
                                    {msg.message}
                                    {isMine && (
                                        <div
                                            style={{
                                                position: "absolute",
                                                bottom: "-18px",
                                                right: "0",
                                                fontSize: "12px",
                                                color: msg.read ? "green" : "gray",
                                                display: "flex",
                                                alignItems: "center",
                                                gap: "4px",
                                            }}
                                        >
                                            <span>{msg.read ? "✔" : "1"}</span>
                                            <span>{msg.read ? "읽음" : "안읽음"}</span>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            <div style={{ marginTop: "10px", display: "flex", gap: "8px" }}>
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="메시지 입력"
                    style={{ flex: 1, padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                />
                <button
                    onClick={sendMessage}
                    style={{
                        padding: "10px 20px",
                        backgroundColor: "#4CAF50",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                    }}
                >
                    전송
                </button>
            </div>
        </div>
    );
};

export default ChatRoom;
