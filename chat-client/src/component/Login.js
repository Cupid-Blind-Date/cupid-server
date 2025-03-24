import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

const Login = ({ setToken }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await axios.post("http://localhost:8080/members/login", {
                username,
                password
            });

            const { accessToken, memberId } = response.data;

            // ✅ localStorage 저장
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("memberId", memberId);

            // 필요하다면 전역 상태로도 저장
            setToken(accessToken);

            navigate("/chatroom");
        } catch (error) {
            console.error("Login failed", error);
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Login</button>
        </div>
    );
};

export default Login;
