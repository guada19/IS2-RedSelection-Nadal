function Profesiones({listado}) {
    return (
        <>
            <section className="content">
                <h2 className="mt-3">Profesiones</h2>
                <div className="list-group shadow-sm p-3 mb-5 rounded">
                    <h4 className="list-group-item list-group-item-action active text-center"
                        aria-current="true">
                        Listado de Profesiones
                    </h4>
                    {listado.map((profesion, index) => (
                        <div key={index} className="list-group-item list-group-item-action">
                            <h5 className="mb-1">{profesion.nombre}</h5>
                        </div>
                    ))}
                </div>
            </section>
        </>
    )
}

export default Profesiones