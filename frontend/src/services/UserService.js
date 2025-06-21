import axios from "axios";
import Cookies from "js-cookie";

const API_URL = import.meta.env.VITE_API_URL

// Send one global header for all secured paths
const api = axios.create({
    headers: {
        Authorization: `Bearer ${Cookies.get("token")}`
    }
})

export const getUserByEmail = (email) => {
    return api.get(API_URL + "/user/find", {params: {email: email}})
}