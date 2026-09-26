// QRuta · Trazabilidad – interfaz de demostración que consume la API REST.

const ETAPAS = ["EN_ORIGEN", "EN_TRANSITO", "EN_DESTINO", "ENTREGADO"];

const DESCRIPCIONES = {
    PRODUCTOR: "Registra productos y asígnalos a un intermediario. Solo ves los productos que creaste.",
    INTERMEDIARIO: "Haz avanzar tus productos asignados por las etapas de la cadena.",
    REGULADOR: "Consulta todos los productos, sus usuarios y el historial completo de cada uno."
};

let usuarioActual = null;

const $ = (id) => document.getElementById(id);


// ---------- Sesión (se recuerda solo en esta pestaña) ----------

function guardarSesion(usuario) {
    try { sessionStorage.setItem("qruta-usuario", JSON.stringify(usuario)); } catch (e) { /* sin almacenamiento */ }
}

function leerSesion() {
    try { return JSON.parse(sessionStorage.getItem("qruta-usuario")); } catch (e) { return null; }
}

function borrarSesion() {
    try { sessionStorage.removeItem("qruta-usuario"); } catch (e) { /* sin almacenamiento */ }
}


// ---------- API ----------

async function api(metodo, url, cuerpo) {

    const cabeceras = { "Content-Type": "application/json" };

    if (usuarioActual) {
        cabeceras["X-Usuario"] = usuarioActual.nombreUsuario;
    }

    const respuesta = await fetch(url, {
        method: metodo,
        headers: cabeceras,
        body: cuerpo ? JSON.stringify(cuerpo) : undefined
    });

    const datos = await respuesta.json().catch(() => null);

    if (!respuesta.ok) {
        throw new Error((datos && datos.mensaje) || "Error " + respuesta.status);
    }

    return datos;
}


// ---------- Avisos ----------

function avisar(mensaje, tipo = "exito") {

    const aviso = document.createElement("div");
    aviso.className = "aviso aviso--" + tipo;
    aviso.textContent = mensaje;

    $("avisos").appendChild(aviso);

    setTimeout(() => aviso.remove(), tipo === "error" ? 6000 : 3500);
}


// ---------- Vistas ----------

function mostrarLogin() {

    usuarioActual = null;
    borrarSesion();

    $("vista-login").hidden = false;
    $("vista-panel").hidden = true;
    $("sesion").hidden = true;
}

async function mostrarPanel() {

    $("vista-login").hidden = true;
    $("vista-panel").hidden = false;
    $("sesion").hidden = false;

    $("sesion-nombre").textContent = usuarioActual.nombreUsuario;
    $("sesion-rol").textContent = usuarioActual.rol;

    const rol = usuarioActual.rol;

    $("titulo-panel").textContent =
        rol === "PRODUCTOR" ? "Mis productos"
            : rol === "INTERMEDIARIO" ? "Productos asignados"
            : "Todos los productos";

    $("descripcion-panel").textContent = DESCRIPCIONES[rol];

    $("panel-crear").hidden = rol !== "PRODUCTOR";
    $("panel-cambio").hidden = rol !== "INTERMEDIARIO";
    $("panel-usuarios").hidden = rol !== "REGULADOR";

    if (rol === "PRODUCTOR") await cargarIntermediarios();
    if (rol === "REGULADOR") await cargarUsuarios();

    await cargarProductos();
}


// ---------- Datos ----------

async function cargarProductos() {

    try {
        const productos = await api("GET", "/api/productos");
        pintarProductos(productos);
    } catch (error) {
        avisar(error.message, "error");
    }
}

async function cargarIntermediarios() {

    const intermediarios = await api("GET", "/api/usuarios/intermediarios");

    $("crear-intermediario").innerHTML = intermediarios
        .map(u => `<option value="${u.nombreUsuario}">${u.nombreUsuario}</option>`)
        .join("");
}

async function cargarUsuarios() {

    const usuarios = await api("GET", "/api/usuarios");

    $("lista-usuarios").innerHTML = usuarios
        .map(u => `<li><span>${u.nombreUsuario}</span><span class="insignia">${u.rol}</span></li>`)
        .join("");
}


// ---------- Pintado ----------

function pintarProductos(productos) {

    $("sin-productos").hidden = productos.length > 0;

    $("lista-productos").innerHTML = productos
        .map(pintarProducto)
        .join("");
}

function pintarProducto(p) {

    const indiceActual = ETAPAS.indexOf(p.estado);

    const etapas = ETAPAS.map((etapa, i) => `
        <div class="etapa ${i <= indiceActual ? "etapa--hecha" : ""} ${i === indiceActual ? "etapa--actual" : ""}">
            <span class="etapa__marca"></span>
            <span>${etapa.replace("_", " ")}</span>
        </div>`).join("");

    const puedeAvanzar =
        usuarioActual.rol === "INTERMEDIARIO" && p.siguienteEstado;

    return `
    <article class="tarjeta producto">
        <div class="producto__cabecera">
            <div>
                <p class="producto__nombre">${escapar(p.nombre)} <span class="producto__id">#${p.id}</span></p>
                <p class="producto__actores">Productor: ${p.productor} · Intermediario: ${p.intermediario}</p>
            </div>
            <span class="estado estado--${p.estado}">${p.estado}</span>
        </div>

        <div class="etapas">${etapas}</div>

        <dl class="datos">
            <div class="dato"><dt>Inicio del transporte</dt><dd>${p.horaInicioTransporte || "—"}</dd></div>
            <div class="dato"><dt>Llegada</dt><dd>${p.horaLlegada || "—"}</dd></div>
            <div class="dato dato--destacado"><dt>Duración</dt><dd>${p.duracionTransporte || "—"}</dd></div>
        </dl>

        <div class="producto__acciones">
            ${puedeAvanzar
                ? `<button class="boton boton--primario boton--pequeno" data-avanzar="${p.id}" data-estado="${p.siguienteEstado}">Pasar a ${p.siguienteEstado}</button>`
                : ""}
            <button class="boton boton--fantasma boton--pequeno" data-historial="${p.id}" data-nombre="${escapar(p.nombre)}">Ver historial</button>
        </div>
    </article>`;
}

function escapar(texto) {
    const div = document.createElement("div");
    div.textContent = texto;
    return div.innerHTML;
}


// ---------- Acciones ----------

async function cambiarEstado(id, estado) {

    try {
        await api("PUT", `/api/productos/${id}/estado`, { estado });
        avisar(`Producto #${id} pasó a ${estado}`);
        await cargarProductos();
    } catch (error) {
        avisar(error.message, "error");
    }
}

async function verHistorial(id, nombre) {

    try {
        const historial = await api("GET", `/api/productos/${id}/historial`);

        $("titulo-historial").textContent = `Historial · ${nombre} #${id}`;

        $("contenido-historial").innerHTML = historial.length === 0
            ? `<p class="texto-suave">Todavía no hay cambios de estado: el producto sigue en EN_ORIGEN.</p>`
            : `<ol class="linea-tiempo">${historial.map(c => `
                <li>
                    <strong>${c.estadoAnterior} → ${c.estadoNuevo}</strong>
                    <span>${c.fechaHora} · por ${c.usuario}</span>
                </li>`).join("")}</ol>`;

        $("dialogo-historial").showModal();
    } catch (error) {
        avisar(error.message, "error");
    }
}


// ---------- Eventos ----------

$("formulario-login").addEventListener("submit", async (evento) => {

    evento.preventDefault();

    try {
        usuarioActual = await api("POST", "/api/login", {
            nombreUsuario: $("login-usuario").value.trim(),
            contrasena: $("login-contrasena").value
        });

        guardarSesion(usuarioActual);
        $("formulario-login").reset();

        await mostrarPanel();
    } catch (error) {
        usuarioActual = null;
        avisar(error.message, "error");
    }
});

document.querySelectorAll("[data-usuario]").forEach(chip => {
    chip.addEventListener("click", () => {
        $("login-usuario").value = chip.dataset.usuario;
        $("login-contrasena").value = "clave123";
        $("login-contrasena").focus();
    });
});

$("boton-salir").addEventListener("click", mostrarLogin);

$("boton-actualizar").addEventListener("click", cargarProductos);

$("formulario-crear").addEventListener("submit", async (evento) => {

    evento.preventDefault();

    try {
        const producto = await api("POST", "/api/productos", {
            nombre: $("crear-nombre").value.trim(),
            intermediario: $("crear-intermediario").value
        });

        avisar(`Producto #${producto.id} registrado en ${producto.estado}`);
        $("crear-nombre").value = "";

        await cargarProductos();
    } catch (error) {
        avisar(error.message, "error");
    }
});

$("formulario-cambio").addEventListener("submit", (evento) => {
    evento.preventDefault();
    cambiarEstado($("cambio-id").value, $("cambio-estado").value);
});

$("lista-productos").addEventListener("click", (evento) => {

    const avanzar = evento.target.closest("[data-avanzar]");
    const historial = evento.target.closest("[data-historial]");

    if (avanzar) cambiarEstado(avanzar.dataset.avanzar, avanzar.dataset.estado);
    if (historial) verHistorial(historial.dataset.historial, historial.dataset.nombre);
});

$("cerrar-historial").addEventListener("click", () => $("dialogo-historial").close());


// ---------- Inicio ----------

usuarioActual = leerSesion();

if (usuarioActual) {
    mostrarPanel().catch(mostrarLogin);
} else {
    mostrarLogin();
}
