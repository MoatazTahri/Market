import React, {useEffect, useState} from 'react';
import "../assets/css/products.scss"
import {getAllProducts} from "../services/ProductService.js";

function ProductsPage() {
    const [products, setProducts] = useState([])
    const API_URL = import.meta.env.VITE_API_URL
    useEffect(() => {
        getAllProducts().then((response) => {
            setProducts(response.data);
        }).catch((error) => {
            console.log(error)
        })
    }, []);
    return (
        <div>
            <div className="text-center p-5">
                <h1> Welcome </h1>
            </div>
            <div className="row container m-auto py-5 row-gap-4">
                {products.length === 0 && <div className="m-auto w-25"> No product found, you can add some by going to your management page in your profile (create account / login first).  </div>}
                {products.map((product, key) =>
                    <div key={key} className="product-card-container col-md-4">
                        <div className="product-card">
                            <div className="image"
                                 style={{background: `url('${API_URL + product.imagePath}') center/contain no-repeat`}}/>
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
                                {product.quantity} in stock
                            </div>
                            <div className="text-center">
                                <button className="btn"> Add to cart</button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ProductsPage;