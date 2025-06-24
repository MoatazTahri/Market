import axiosInstance from "./axiosInstance.js";

export const register = (user = {}) => {
    return axiosInstance.post("/auth/register", user)
}
export const authenticate = (email, password) => {
    return axiosInstance.post("/auth/login", {email: email, password: password})
}
export const extractTokenClaims = (token) => {
    return axiosInstance.get("/auth/extract-claims", {params : {token: token}})
}