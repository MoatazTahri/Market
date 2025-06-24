import axiosInstance from "./axiosInstance.js";




export const createProduct = (product) => {
    return axiosInstance.post("/product/create", product)
}

export const updateProduct = (product) => {
    return axiosInstance.put("/product/update", product)
}

export const getAllProducts = () => {
    return axiosInstance.get("/product/all")
}

export const getAllProductsByUserId = (userId) => {
    return axiosInstance.get(`/product/all/${userId}`)
}

export const getProductById = (id) => {
    return axiosInstance.get("/product/" + id)
}

export const getAllProductCategories = () => {
    return axiosInstance.get("/product/categories")
}

export const deleteProduct = (id) => {
    return axiosInstance.delete("/product/delete/" + id)
}