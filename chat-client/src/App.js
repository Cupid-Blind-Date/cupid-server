import React, {useState} from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import Login from "./component/Login";
import ChatRoom from "./component/ChatRoom";

const App = () => {
    const [token, setToken] = useState(null);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login setToken={setToken} />} />
                <Route path="/chatroom" element={token ? <ChatRoom roomId={1} token={token} /> : <Navigate to="/" />} />
            </Routes>
        </Router>
    );
};

export default App;
