import axios from "axios";
import { jwtDecode } from "jwt-decode";

const API_URL = import.meta.env.VITE_API_URL;

const axiosInstance = axios.create({
    baseURL: API_URL,
})

// Auto-refresh access token if it's expired
axiosInstance.interceptors.request.use(async (config) => {
    let accessToken = localStorage.getItem("accessToken");
    let refreshToken = localStorage.getItem("refreshToken");

    if (accessToken) {
        const decoded = jwtDecode(accessToken);
        const now = Date.now() / 1000;

        // If the access token expired we refresh it calling the api
        if (decoded.exp < now) {
            axios.post(API_URL + "/auth/refresh-token?refreshToken=" + refreshToken)
                .then((response) => {
                    localStorage.setItem("accessToken", response.data)
            })
        }
        config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
});


export default axiosInstance;