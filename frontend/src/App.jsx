import {BrowserRouter, Route, Routes, useLocation} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.min.js"
import "./assets/css/shared.css"
import {useEffect, useState} from "react";
import Header from "./layouts/Header.jsx";
import ProductsPage from "./components/ProductsPage.jsx";
import LoginForm from "./components/LoginForm.jsx";
import SignupForm from "./components/SignupForm.jsx";

function AppWithRoutes() {
    const pathsWithoutHeaderAndFooter = ["/login", "/register"]
    const location = useLocation();
    const [headerIncluded, setHeaderIncluded] = useState(true)
    const [footerIncluded, setFooterIncluded] = useState(true) // Just in case of footer is not included while the header is
    useEffect(() => {
        setHeaderIncluded(!pathsWithoutHeaderAndFooter.includes(location.pathname))
        setFooterIncluded(!pathsWithoutHeaderAndFooter.includes(location.pathname))
    }, [location.pathname]);

    return (
        <>
            {headerIncluded && <Header/>}
            <Routes>
                <Route path="/login" Component={LoginForm}/>
                <Route path="/register" Component={SignupForm}/>
                <Route path="/products" Component={ProductsPage}/>
                <Route path="/" Component={ProductsPage}/>
            </Routes>
        </>
    )
}

export default function App() {
    return (
        <BrowserRouter>
            <AppWithRoutes/>
        </BrowserRouter>
    )
}
