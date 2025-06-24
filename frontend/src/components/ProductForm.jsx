import React, {useEffect, useRef, useState} from 'react';
import {createProduct, getAllProductCategories, getProductById, updateProduct} from "../services/ProductService.js";
import "../assets/css/product-form.scss"
import {getUserByEmail} from "../services/UserService.js";
import {useNavigate, useParams} from "react-router-dom";

function ProductForm() {
    const API_URL = import.meta.env.VITE_API_URL
    const navigate = useNavigate()
    const {id} = useParams()
    const imageRef = useRef(null)
    const [categories, setCategories] = useState([])
    const [name, setName] = useState('')
    const [skuCode, setSkuCode] = useState('')
    const [description, setDescription] = useState('')
    const [price, setPrice] = useState(0)
    const [quantity, setQuantity] = useState(0)
    const [selectedCategory, setSelectedCategory] = useState('')
    const [image, setImage] = useState(null)
    const [imagePreview, setImagePreview] = useState(null)
    const [currentUserId, setCurrentUserId] = useState()
    const [errors, setErrors] = useState({})
    useEffect(() => {
        getAllProductCategories().then((response) => {
            setCategories(response.data)
            setSelectedCategory(response.data[0])
        }).catch((error) => {
            console.log("error here")
            console.log(error)
        })
    }, []);
    useEffect(() => {
        getUserByEmail(localStorage.getItem("email")).then((response) => {
            setCurrentUserId(response.data.id)
        }).catch((error) => {
            console.log(error)
        })
    })
    useEffect(() => {
        if (id) { // Product id
            getProductById(id).then(async (response) => {
                const product = response.data
                const imageUrl = API_URL + product.imagePath
                setName(product.name)
                setSkuCode(product.skuCode)
                setDescription(product.description)
                setPrice(product.price)
                setSelectedCategory(product.category)
                setQuantity(product.stock)
                setImagePreview(imageUrl)

                // 👉 Convert image URL to Blob/File
                const imageResponse = await fetch(imageUrl);
                const blob = await imageResponse.blob();

                // Create a File object with the correct name and type
                const filename = product.imagePath.split("/").pop();
                const file = new File([blob], filename, {type: blob.type});
                setImage(file);
            }).catch((error) => {
                console.log(error)
            })
        }
    }, [])

    function handleSubmit() {
        const product = new FormData();
        product.append("name", name);
        product.append("skuCode", skuCode);
        product.append("description", description);
        product.append("price", price);
        product.append("stock", quantity);
        product.append("category", selectedCategory);
        product.append("sellerId", currentUserId);
        if (image) {
            product.append("image", image); // to prevent backend from receiving image "null"/"undefined" as string
        }
        if (id) {
            product.append("id", id);
            updateProduct(product).then(() => {
                alert("Product updated successfully")
                navigate("/my-products")
            }).catch((error) => {
                console.log(error)
                if (error.response?.status === 422) {
                    setErrors(error.response.data)
                }
            })
        } else {
            createProduct(product).then(() => {
                alert("Product added successfully")
                navigate("/my-products")
            }).catch((error) => {
                console.log(error)
                if (error.response?.status === 422) {
                    setErrors(error.response.data)
                }
            })
        }
    }

    function handleChange(e) {
        setImagePreview(URL.createObjectURL(e.target.files[0]));
        setImage(e.target.files[0])
    }

    const uploadImage = () => {
        imageRef.current.click()
    }

    const removeImage = () => {
        setImage(null)
        setImagePreview(null)
    }

    return (
        <div style={{padding: "50px 100px 100px 100px"}}>
            <div className="header" style={{marginBottom: "50px"}}>
                <h1> {id ? 'Update Product' : 'Create Product'} </h1>
            </div>
            <div className="form-container row">
                <div className="col-lg-6">
                    <div className="right-container">
                        <div className="form">
                            <div className="form-group">
                                <span> Product name <span className="star">*</span> </span>
                                <input type="text"
                                       placeholder="Product name"
                                       onChange={(e) => setName(e.target.value)}
                                       value={name}
                                />
                                <span className="error">{errors.name}</span>
                            </div>
                            <div className="form-group">
                                <span> SKU Code <span className="star">*</span> </span>
                                <input type="text"
                                       placeholder="SKU Code"
                                       onChange={(e) => setSkuCode(e.target.value)}
                                       value={skuCode}
                                />
                                <span className="error">{errors.skuCode}</span>
                            </div>
                            <div className="form-group">
                                <span> Description <span className="star">*</span> </span>
                                <textarea placeholder="Description"
                                          onChange={(e) => setDescription(e.target.value)}
                                          value={description}
                                />
                                <span className="error">{errors.description}</span>
                            </div>
                            <div className="form-group">
                                <span> Price ($) <span className="star">*</span> </span>
                                <input type="number"
                                       step={0.01}
                                       min={0}
                                       placeholder="Price"
                                       onChange={(e) => setPrice(e.target.value)}
                                       value={price}
                                />
                                <span className="error">{errors.price}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-lg-6">
                    <div className="left-container">
                        {/* Article image input */}
                        <div className="form">
                            <div className="form-group">
                                <span> Category <span className="star">*</span> </span>
                                <select value={selectedCategory} className="form-select"
                                        onChange={(e) => setSelectedCategory(e.target.value)}>
                                    {categories.map((item, index) => (
                                        <option key={index} value={item}>
                                            {item}
                                        </option>
                                    ))}
                                </select>
                                <span className="error">{errors.category}</span>
                            </div>
                            <div className="form-group">
                                <span> Quantity <span className="star">*</span> </span>
                                <input type="text"
                                       placeholder="Quantity"
                                       onChange={(e) => setQuantity(e.target.value.replace(/[^0-9]/g, ''))}
                                       value={quantity}
                                />
                                <span className="error">{errors.stock}</span>
                            </div>
                            <div className="form-group">
                                <span> Image <span className="star">*</span> </span>
                                {imagePreview ?
                                    <div className="image-upload-preview"
                                         style={{background: `url('${imagePreview}') center/contain no-repeat`}}>
                                        <div className="icon-container" onClick={removeImage}>
                                            <i className="fa fa-remove"/>
                                        </div>
                                    </div>
                                    :
                                    <div className="image-input" onClick={uploadImage}>
                                        <div className="d-grid text-center">
                                            <span> Insert product image </span>
                                            <i className="fa fa-add"/>
                                        </div>
                                        <input ref={imageRef} type="file" accept="image/*" style={{display: "none"}}
                                               onChange={handleChange}/>
                                    </div>
                                }
                                <span className="error">{errors.image}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div className="text-center pt-5">
                <button className="btn main-button" onClick={handleSubmit}> {id ? 'Update' : 'Add'} </button>
            </div>
        </div>
    );
}

export default ProductForm;