import React, {useEffect, useState} from 'react';
import "../assets/css/header.scss"
import {extractTokenClaims} from "../services/AuthService.js";
import {getUserByEmail} from "../services/UserService.js";
import Cookies from "js-cookie";
import {useNavigate} from "react-router-dom";

function Header() {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    const navigate = useNavigate()
    useEffect(() => {
        const token = Cookies.get("token")
        if (token !== undefined) {
            extractTokenClaims(token).then((response) => {
                setEmail(response.data.email)
                setIsLoggedIn(true)
                getUserByEmail(email).then((response) => {
                    setFirstName(response.data.firstName)
                    setLastName(response.data.lastName)
                }).catch((error) => {
                    console.log(error)
                })
            }).catch((error) => {
                console.log(error)
            })
        }
    }, [email]);
    function logout() {
        Cookies.remove("token")
        setIsLoggedIn(false)
        setFirstName('')
        setLastName('')
        setEmail('')
        navigate("/login")
    }
    return (
        <nav className="website-navbar navbar navbar-expand-lg">
            <a className="navbar-brand brand" href="/">
                Market
            </a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse justify-content-between" id="navbarNav">
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <a className="nav-link" href="#">Home</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/products">Shop</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="#">About us</a>
                    </li>
                </ul>
                {isLoggedIn ?
                    <>
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <a className="nav-link">{firstName + " " + lastName}</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" onClick={logout}>Logout</a>
                        </li>
                    </ul>
                    </>
                    :
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <a className="nav-link" href="/login">Login</a>
                        </li>
                    </ul>

                }
            </div>
        </nav>
    );
}

export default Header;