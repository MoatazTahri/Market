import {BrowserRouter, Navigate, Route, Routes, useLocation} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.min.js"
import "@fortawesome/fontawesome-free/css/all.css"
import "./assets/css/shared.css"
import "./assets/css/footer.scss"
import {useEffect, useState} from "react";
import Header from "./layouts/Header.jsx";
import ProductsPage from "./components/ProductsPage.jsx";
import LoginForm from "./components/LoginForm.jsx";
import SignupForm from "./components/SignupForm.jsx";
import ProductForm from "./components/ProductForm.jsx";
import {extractTokenClaims} from "./services/AuthService.js";
import ProductsManagementPage from "./components/ProductsManagementPage.jsx";
import Footer from "./layouts/Footer.jsx";

function AppWithRoutes() {
    const [isAuthenticated, setIsAuthenticated] = useState(null)
    const pathsWithoutHeaderAndFooter = ["/login", "/register"]
    const location = useLocation();
    const [headerIncluded, setHeaderIncluded] = useState(true)
    const [footerIncluded, setFooterIncluded] = useState(true) // Just in case of footer is not included while the header is
    useEffect(() => {
        setHeaderIncluded(!pathsWithoutHeaderAndFooter.includes(location.pathname))
        setFooterIncluded(!pathsWithoutHeaderAndFooter.includes(location.pathname))
    }, [location.pathname]);

    useEffect(() => {
        const token = localStorage.getItem("refreshToken")
        if (token) {
            extractTokenClaims(token).then((response) => { // if token exists but expired authentication will be "false".
                setIsAuthenticated(true)
            }).catch((error) => {
                setIsAuthenticated(false)
                console.log(error)
            })
        } else {
            setIsAuthenticated(false)
        }
    }, []);

    const ProtectedRoute = ({children}) => {
        if (isAuthenticated === null) {
            // Loading while checking for token validity.
            return (
                <div style={{height: "100vh", display: "flex", justifyContent: "center", alignItems: "center"}}>
                    <div className="spinner-border text-primary" role="status">
                        <span className="sr-only">Loading...</span>
                    </div>
                </div>
            )
        }

        if (!isAuthenticated) {
            return <Navigate to="/login"/>;
        }

        return <>{children}</>;
    }

    return (
        <>
            {headerIncluded && <Header/>}
            <Routes>
                <Route path="/login" Component={LoginForm}/>
                <Route path="/register" Component={SignupForm}/>
                <Route path="/products" Component={ProductsPage}/>
                <Route path="/" Component={ProductsPage}/>
                <Route path="/product/create" element={
                    <ProtectedRoute>
                        <ProductForm key={location.key}/>
                    </ProtectedRoute>
                }/>
                <Route path="/my-products" element={
                    <ProtectedRoute>
                        <ProductsManagementPage key={location.key}/>
                    </ProtectedRoute>
                }/>
                <Route path="/my-products/update/:id" element={
                    <ProtectedRoute>
                        <ProductForm key={location.key}/>
                    </ProtectedRoute>
                }/>
            </Routes>
            {footerIncluded && <Footer/>}
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
