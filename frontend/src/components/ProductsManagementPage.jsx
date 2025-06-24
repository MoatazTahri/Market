import React, {useEffect, useState} from 'react';
import {deleteProduct, getAllProducts, getAllProductsByUserId} from "../services/ProductService.js";
import "../assets/css/products.scss"
import {getUserByEmail} from "../services/UserService.js";
import {useNavigate} from "react-router-dom";

function ProductsManagementPage() {
    const API_URL = import.meta.env.VITE_API_URL
    const [products, setProducts] = useState([])
    const [currentUserId, setCurrentUserId] = useState()
    const navigate = useNavigate()
    useEffect(() => {
        const email = localStorage.getItem("email")
        if (email) {
            getUserByEmail(email).then((response) => {
                setCurrentUserId(response.data.id)
            }).catch((error) => {
                console.log(error)
            })
        }
    }, []);
    useEffect(() => {
        if (currentUserId) {
            getAllProductsByUserId(currentUserId).then((response) => {
                setProducts(response.data);
            }).catch((error) => {
                console.log(error)
            })
        }
    }, [currentUserId]);

    function handleDelete(id) {
        confirm("Are you sure you want to delete this product?") && deleteProduct(id).then((response) => {
            window.location.reload()
        }).catch((error) => {
            console.log(error)
        })
    }

    return (
        <div>
            <div className="text-center p-5">
                <h1> Products Management </h1>
            </div>
            <div className="text-center">
                <button className="btn main-button" onClick={() => navigate('/product/create')}> Add new product <i className="fa fa-add"/> </button>
            </div>
            <div className="row container m-auto py-5 row-gap-4">
                {products.map((product, key) =>
                    <div key={key} className="product-card-container col-md-4">
                        <div className="product-card management">
                            <div className="image"
                                 style={{background: `url('${API_URL + '' + product.imagePath}') center/contain no-repeat`}}/>
                            <div className="category">
                                {product.category}
                            </div>
                            <div className="name">
                                {product.name.toUpperCase()}
                            </div>
                            <div className="price">
                                ${product.price.toFixed(2)}
                            </div>
                            <div className="quantity">
                                {product.stock} in stock
                            </div>
                            <div className="text-center">
                                <button className="btn update" onClick={() => navigate('/my-products/update/' + product.id)}> Update </button>
                                <button className="btn delete" onClick={() => handleDelete(product.id)}> Delete </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ProductsManagementPage;