import React, {useEffect, useState} from 'react';
import "../assets/css/header.scss"
import {getUserByEmail} from "../services/UserService.js";
import {useNavigate} from "react-router-dom";

function Header() {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [isLoggedIn, setIsLoggedIn] = useState()
    const navigate = useNavigate()
    useEffect(() => {
        const token = localStorage.getItem("refreshToken")
        const storedEmail = localStorage.getItem("email")
        if (token) {
            getUserByEmail(storedEmail).then((response) => {
                setFirstName(response.data.firstName)
                setLastName(response.data.lastName)
                setIsLoggedIn(true)
            }).catch((error) => {
                console.log(error)
                setIsLoggedIn(false)
            })
        }
    }, [isLoggedIn]);

    function logout() {
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        localStorage.removeItem("email")
        setIsLoggedIn(false)
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
                    <div className="d-flex align-items-center">
                        <div className="profile-picture order-lg-2 ms-lg-2"
                             style={{background: "url('/img/default-profile-picture.png') center/cover no-repeat"}}></div>
                        <div className="dropdown order-lg-1">
                            <button className="dropdown-toggle" type="button" id="dropdownMenu2"
                                    data-bs-toggle="dropdown" aria-expanded="false">
                                {firstName} {lastName}
                            </button>
                            <ul className="dropdown-menu dropdown-menu-lg-end" aria-labelledby="dropdownMenu2">
                                <li>
                                    <button className="dropdown-item" type="button"
                                            onClick={() => navigate('/my-products')}>Management
                                    </button>
                                </li>
                                <li>
                                    <button className="dropdown-item" type="button" onClick={logout}>Logout</button>
                                </li>
                            </ul>
                        </div>
                    </div>
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