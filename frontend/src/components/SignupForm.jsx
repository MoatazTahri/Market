import React, {useState} from 'react';
import "../assets/css/auth-form.scss"
import {register} from "../services/AuthService.js";
import {useNavigate} from "react-router-dom";

function SignupForm() {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState(null)
    const [confirmPassword, setConfirmPassword] = useState('')
    const [errors, setErrors] = useState({})
    const navigate = useNavigate()

    function onSubmit(e) {
        e.preventDefault()
        const user = {firstName, lastName, email, password}
        if (password !== confirmPassword) {
            setErrors({confirmPassword: "Passwords don't match"})
            return
        }
        register(user)
            .then(() => {
                setErrors({})
                setFirstName('')
                setLastName('')
                setEmail('')
                setPassword('')
                setConfirmPassword('')
                alert("User created successfully")
                navigate("/login")
            })
            .catch((error) => {
                console.log(error)
                if (error.response.status === 422) {
                    let newErrors = error.response.data;
                    // Check if the password is not null and has no error before checking with the confirmed password
                    if (password !== null && password !== confirmPassword && newErrors.password === undefined) {
                        newErrors = {
                            ...newErrors,
                            confirmPassword: "Passwords don't match"
                        };
                    }
                    setErrors(newErrors);
                }
            })

    }

    return (
        <div>
            <section className="body">
                <div className="container">
                    <div className="login-box">
                        <div className="row">
                            <div className="logo">
                                <h3 className="header-title">Sign Up</h3>
                            </div>
                        </div>
                        <div className="container">
                            <div className="login-form">
                                <div className="form-group">
                                    <input type="text"
                                           className="form-control"
                                           placeholder="First Name"
                                           value={firstName}
                                           onChange={(e) => setFirstName(e.target.value)}
                                    />
                                    <span className="text-danger">{errors.firstName}</span>
                                </div>
                                <div className="form-group">
                                    <input type="text"
                                           className="form-control"
                                           placeholder="Last Name"
                                           value={lastName}
                                           onChange={(e) => setLastName(e.target.value)}
                                    />
                                    <span className="text-danger">{errors.lastName}</span>
                                </div>
                                <div className="form-group">
                                    <input type="text"
                                           className="form-control"
                                           placeholder="Email"
                                           value={email}
                                           onChange={(e) => setEmail(e.target.value)}
                                    />
                                    <span className="text-danger">{errors.email}</span>
                                </div>
                                <div className="form-group">
                                    <input type="Password"
                                           className="form-control"
                                           placeholder="Password"
                                           value={password}
                                           onChange={(e) => setPassword(e.target.value)}
                                    />
                                    <span className="text-danger">{errors.password}</span>
                                </div>
                                <div className="form-group">
                                    <input type="Password"
                                           className="form-control"
                                           placeholder="Confirm password"
                                           value={confirmPassword}
                                           onChange={(e) => setConfirmPassword(e.target.value)}
                                    />
                                    <span className="text-danger">{errors.confirmPassword}</span>
                                </div>
                                <div className="text-center">
                                    <button className="btn btn-block" onClick={onSubmit}>Sign Up</button>
                                </div>
                                <div className="form-group">
                                    <div className="text-center"> Already have an account? <a
                                        href="/login"> Login </a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}

export default SignupForm;