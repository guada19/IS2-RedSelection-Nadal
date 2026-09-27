import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
    Box,
    IconButton,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Snackbar,
    Alert,
    Card,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TablePagination,
    CircularProgress
} from '@mui/material';

import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';

export default function Products() {
    const [products, setProducts] = useState([]);
    const [cargando, setCargando] = useState(true);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');

    const [filterText, setFilterText] = useState("");
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);

    const [openAdd, setOpenAdd] = useState(false);
    const [openEdit, setOpenEdit] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState(null);

    
    const [newProduct, setNewProduct] = useState({
        nombre: '',
        descripcion: '',
        marca: '',
        fechaAdquisicion: '',
        precio: ''
    });

   
    const [editProduct, setEditProduct] = useState({
        id: null,
        nombre: '',
        descripcion: '',
        marca: '',
        fechaAdquisicion: '',
        precio: ''
    });

    const cargarProductos = () => {
        setCargando(true);
        axios.get('http://localhost:8080/products')
            .then(response => {
                setProducts(response.data);
                setCargando(false);
            })
            .catch(error => {
                console.error("Error al cargar productos:", error);
                setSnackbarMessage("Error al conectar con el servidor.");
                setSnackbarSeverity("error");
                setSnackbarOpen(true);
                setCargando(false);
            });
    };

    useEffect(() => {
        cargarProductos();
    }, []);

    const handleChangeAdd = (e) => {
        setNewProduct({ ...newProduct, [e.target.name]: e.target.value });
    };

    const handleChangeEdit = (e) => {
        setEditProduct({ ...editProduct, [e.target.name]: e.target.value });
    };

    const handleConfirmOpen = (id) => {
        setDeleteId(id);
        setConfirmOpen(true);
    };

    const handleConfirmClose = () => {
        setDeleteId(null);
        setConfirmOpen(false);
    };

    const handleClickOpenEdit = (product) => {
        setEditProduct(product);
        setOpenEdit(true);
    };

    const handleCloseEdit = () => {
        setOpenEdit(false);
    };

    const handleCloseAdd = () => {
        setOpenAdd(false);
        setNewProduct({
            nombre: '',
            descripcion: '',
            marca: '',
            fechaAdquisicion: '',
            precio: ''
        });
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    
    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/product/${id}`);
            setProducts(products.filter(p => p.id !== id));
            setSnackbarMessage("¡Producto eliminado correctamente!");
            setSnackbarSeverity("success");
            handleConfirmClose();
        } catch (error) {
            console.error("Error al eliminar:", error);
            setSnackbarMessage("Ocurrió un error al intentar eliminar el producto.");
            setSnackbarSeverity("error");
        }
        setSnackbarOpen(true);
    };

   
    const handleAddProduct = async () => {
        try {
            const response = await axios.post('http://localhost:8080/products', {
                ...newProduct,
                precio: parseFloat(newProduct.precio) || 0
            });
            setProducts([...products, response.data]);
            setSnackbarMessage("¡Producto agregado correctamente!");
            setSnackbarSeverity("success");
            handleCloseAdd();
        } catch (error) {
            console.error("Error al crear:", error);
            setSnackbarMessage("Ocurrió un error al intentar crear el producto.");
            setSnackbarSeverity("error");
        }
        setSnackbarOpen(true);
    };

    
    const handleEditProduct = async () => {
        try {
            const response = await axios.put(`http://localhost:8080/product/${editProduct.id}`, {
                ...editProduct,
                precio: parseFloat(editProduct.precio) || 0
            });
            setProducts(products.map(p => p.id === editProduct.id ? response.data : p));
            setSnackbarMessage("¡Producto modificado correctamente!");
            setSnackbarSeverity("success");
            handleCloseEdit();
        } catch (error) {
            console.error("Error al modificar:", error);
            setSnackbarMessage("Ocurrió un error al intentar modificar el producto.");
            setSnackbarSeverity("error");
        }
        setSnackbarOpen(true);
    };

    
    const filteredProducts = products.filter(product =>
        (product.nombre && product.nombre.toLowerCase().includes(filterText.toLowerCase())) ||
        (product.descripcion && product.descripcion.toLowerCase().includes(filterText.toLowerCase())) ||
        (product.marca && product.marca.toLowerCase().includes(filterText.toLowerCase()))
    );

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '85vh',
                padding: '2rem',
                width: '100%'
            }}
        >
            <Card sx={{ width: '90%', padding: '2rem', border: '1px solid #e0e0e0', borderRadius: '8px', boxShadow: 3 }}>
                <TableContainer>
                    <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
                        <Button variant='contained' color="primary" onClick={() => setOpenAdd(true)}>
                            Agregar Nuevo Producto
                        </Button>

                        <TextField
                            label="Buscar por nombre, descripción o marca..."
                            variant="outlined"
                            size="small"
                            sx={{ width: '350px' }}
                            value={filterText}
                            onChange={(e) => {
                                setFilterText(e.target.value);
                                setPage(0);
                            }}
                        />
                    </Box>

                    <Table aria-label="tabla de productos">
                        <TableHead>
                            <TableRow>
                                <TableCell sx={{ fontWeight: 'bold' }}>#</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Nombre</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Descripción</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Marca</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Fecha de Adquisición</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Precio</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }} align='center'>Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {cargando ? (
                                <TableRow>
                                    <TableCell colSpan={7} align="center">
                                        <CircularProgress size={24} sx={{ mr: 1, verticalAlign: 'middle' }} />
                                        Cargando productos...
                                    </TableCell>
                                </TableRow>
                            ) : filteredProducts.length > 0 ? (
                                filteredProducts
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((producto, index) => (
                                        <TableRow key={producto.id}>
                                            <TableCell>{page * rowsPerPage + index + 1}</TableCell>
                                            <TableCell sx={{ fontWeight: 500 }}>{producto.nombre}</TableCell>
                                            <TableCell>{producto.descripcion}</TableCell>
                                            <TableCell>{producto.marca}</TableCell>
                                            <TableCell>{producto.fechaAdquisicion}</TableCell>
                                            <TableCell>${Number(producto.precio).toFixed(2)}</TableCell>
                                            <TableCell align='center'>
                                                <IconButton color='primary' onClick={() => handleClickOpenEdit(producto)}>
                                                    <EditIcon />
                                                </IconButton>
                                                <IconButton color='error' onClick={() => handleConfirmOpen(producto.id)}>
                                                    <DeleteIcon />
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={7} align="center">
                                        No se encontraron productos.
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>

                    <TablePagination
                        component="div"
                        count={filteredProducts.length}
                        page={page}
                        onPageChange={handleChangePage}
                        rowsPerPage={rowsPerPage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                        rowsPerPageOptions={[5, 10, 25]}
                        labelRowsPerPage="Filas por página:"
                        labelDisplayedRows={({ from, to, count }) => `${from}-${to} de ${count !== -1 ? count : `más de ${to}`}`}
                    />
                </TableContainer>
            </Card>

            {/* Modal de confirmación para eliminar */}
            <Dialog open={confirmOpen} onClose={handleConfirmClose} maxWidth="xs" fullWidth>
                <DialogTitle>Confirmar Eliminación</DialogTitle>
                <DialogContent>
                    ¿Estás seguro de que deseas eliminar este producto?
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleConfirmClose} color="inherit">
                        Cancelar
                    </Button>
                    <Button onClick={() => handleDelete(deleteId)} color="error" variant="contained">
                        Eliminar
                    </Button>
                </DialogActions>
            </Dialog>

            
            <Dialog open={openAdd} onClose={handleCloseAdd} maxWidth="sm" fullWidth>
                <DialogTitle>Agregar Nuevo Producto</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                        <TextField
                            name="nombre"
                            label="Nombre del Producto"
                            type="text"
                            fullWidth
                            value={newProduct.nombre}
                            onChange={handleChangeAdd}
                        />
                        <TextField
                            name="descripcion"
                            label="Descripción"
                            type="text"
                            fullWidth
                            multiline
                            rows={2}
                            value={newProduct.descripcion}
                            onChange={handleChangeAdd}
                        />
                        <TextField
                            name="marca"
                            label="Marca"
                            type="text"
                            fullWidth
                            value={newProduct.marca}
                            onChange={handleChangeAdd}
                        />
                        <TextField
                            name="fechaAdquisicion"
                            label="Fecha de Adquisición"
                            type="date"
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                            value={newProduct.fechaAdquisicion}
                            onChange={handleChangeAdd}
                        />
                        <TextField
                            name="precio"
                            label="Precio"
                            type="number"
                            fullWidth
                            value={newProduct.precio}
                            onChange={handleChangeAdd}
                        />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseAdd} color="inherit">
                        Cancelar
                    </Button>
                    <Button onClick={handleAddProduct} color="primary" variant="contained">
                        Guardar Producto
                    </Button>
                </DialogActions>
            </Dialog>

            
            <Dialog open={openEdit} onClose={handleCloseEdit} maxWidth="sm" fullWidth>
                <DialogTitle>Editar Producto</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                        <TextField
                            name="nombre"
                            label="Nombre del Producto"
                            type="text"
                            fullWidth
                            value={editProduct.nombre || ''}
                            onChange={handleChangeEdit}
                        />
                        <TextField
                            name="descripcion"
                            label="Descripción"
                            type="text"
                            fullWidth
                            multiline
                            rows={2}
                            value={editProduct.descripcion || ''}
                            onChange={handleChangeEdit}
                        />
                        <TextField
                            name="marca"
                            label="Marca"
                            type="text"
                            fullWidth
                            value={editProduct.marca || ''}
                            onChange={handleChangeEdit}
                        />
                        <TextField
                            name="fechaAdquisicion"
                            label="Fecha de Adquisición"
                            type="date"
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                            value={editProduct.fechaAdquisicion || ''}
                            onChange={handleChangeEdit}
                        />
                        <TextField
                            name="precio"
                            label="Precio"
                            type="number"
                            fullWidth
                            value={editProduct.precio || ''}
                            onChange={handleChangeEdit}
                        />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseEdit} color="inherit">
                        Cancelar
                    </Button>
                    <Button onClick={handleEditProduct} color="primary" variant="contained">
                        Actualizar Producto
                    </Button>
                </DialogActions>
            </Dialog>

            
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={4000}
                onClose={handleSnackbarClose}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert onClose={handleSnackbarClose} severity={snackbarSeverity} sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Box>
    );
}