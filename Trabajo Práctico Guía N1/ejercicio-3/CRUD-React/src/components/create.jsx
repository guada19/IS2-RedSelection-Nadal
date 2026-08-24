import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { collection, addDoc } from "firebase/firestore";

import { db } from "../firebaseConfig/firebase";

const Create = () => {

    const [ description, setDescription ] = useState('')
    const [ stock, setStock ] = useState(0)
    const navigate = useNavigate()

    const productsCollections = collection(db, "products")

    const store = async (e) => {
        e.preventDefault()
        await addDoc( productsCollections, { description: description, stock: Number(stock) } )
        navigate('/')
    }

    return (
        <div className="container mt-4">
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h1>Create Product</h1>

                    <form onSubmit={store}>
                        <div className="mb-3">
                            <label className="form-label">Description</label>
                            <input 
                                value={description} 
                                onChange={ (e)=> setDescription(e.target.value)} 
                                type="text" 
                                className="form-control"
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Stock</label>
                            <input 
                                value={stock} 
                                onChange={ (e)=> setStock(e.target.value)} 
                                type="number" 
                                className="form-control"
                            />
                        </div>

                        <button type="submit" className="btn btn-primary">Store</button>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default Create