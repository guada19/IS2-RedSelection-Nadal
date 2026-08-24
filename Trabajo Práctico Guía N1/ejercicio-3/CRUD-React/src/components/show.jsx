import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { collection, getDocs, doc, deleteDoc } from 'firebase/firestore'
import { db } from '../firebaseConfig/firebase' // Verificar que en firebase.js se exporte como 'db'
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'

const MySwal = withReactContent(Swal)

const Show = () => {
    const [products, setProducts] = useState([])
    const productsCollection = collection(db, "products")

    const getProducts = async () => {
        const data = await getDocs(productsCollection)
        setProducts(
            data.docs.map((doc) => ({ ...doc.data(), id: doc.id }))
        )
    }

    const deleteProduct = async (id) => {
        const productDoc = doc(db, "products", id)
        await deleteDoc(productDoc)
        getProducts()
    }

    const confirmDelete = (id) => {
        MySwal.fire({
            title: <strong>¿Eliminar el producto?</strong>,
            text: "¡No podrás revertir esta acción!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: '<i className="bi bi-trash-fill"></i> Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteProduct(id)
                Swal.fire({
                    title: '¡Eliminado!',
                    text: 'El producto ha sido eliminado.',
                    icon: 'success'
                })
            }
        })
    }

    useEffect(() => {
        getProducts()
    }, [])

    return (
        <div className='container'>
            <div className='row'>
                <div className='col'>
                    <div className='d-grid gap-2'>
                        <Link to="/create" className='btn btn-secondary mt-2 mb-2'>Create</Link>
                    </div>

                    <table className='table table-dark table-hover'>
                        <thead>
                            <tr>
                                <th>Descripción</th>
                                <th>Stock</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {products.map((product) => (
                                <tr key={product.id}>
                                    <td>{product.description}</td>
                                    <td>{product.stock}</td>
                                    <td>
                                        <Link to={`/edit/${product.id}`} className='btn btn-light me-2'><i className="bi bi-pencil-square"></i></Link>
                                        <button onClick={() => confirmDelete(product.id)} className='btn btn-danger'><i className="bi bi-trash-fill"></i></button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
}

export default Show