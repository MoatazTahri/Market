import axiosInstance from "./axiosInstance.js";

export const getUserByEmail = (email) => {
    return axiosInstance.get("/user/find", {params: {email: email}})
}