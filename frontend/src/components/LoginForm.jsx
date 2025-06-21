import React, {useState} from 'react';
import "../assets/css/auth-form.scss"
import {authenticate} from "../services/AuthService.js";
import Cookies from "js-cookie";
import {useNavigate} from "react-router-dom";

function LoginForm() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const navigate = useNavigate()

    function login(e) {
        e.preventDefault()
        authenticate(email, password).then((response) => {
            Cookies.remove("token")
            const token = response.data.token;
            Cookies.set("token", token, {
                expires: 1, // 1 day
                secure: false, // for developing
                sameSite: "strict"
            })
            alert("Login successful")
            navigate("/")
        }).catch((error) => {
            console.log(error)
            alert("Invalid email or password")
        })
    }
    return (
        <div>
            <section className="body">
                <div className="container">
                    <div className="login-box">
                        <div className="row">
                            <div className="logo">
                                <h3 className="header-title">Login</h3>
                            </div>
                        </div>
                        <div className="container">
                            <div className="login-form">
                                <div className="form-group">
                                    <input type="text" className="form-control" placeholder="Email"
                                           value={email}
                                           onChange={(e) => setEmail(e.target.value)}
                                    />
                                </div>
                                <div className="form-group">
                                    <input type="Password" className="form-control" placeholder="Password"
                                           value={password}
                                           onChange={(e) => setPassword(e.target.value)}
                                    />
                                </div>
                                <div className="text-center">
                                    <button className="btn btn-block" onClick={login}>Login</button>
                                </div>
                                <div className="form-group">
                                    <div className="text-center"> New user? <a href="/register"> Join us </a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}

export default LoginForm;