const API = "/api";

const NAV = [
    ["index.html", "Inicio", "🏠"],
    ["catalogo.html", "Catálogo", "📚"],
    ["alumnos.html", "Alumnos", "🧑‍🎓"],
    ["matricula.html", "Matrícula", "📝"],
    ["pagos.html", "Pagos", "💵"],
    ["estado-cuenta.html", "Estado de cuenta", "📄"],
    ["conciliacion.html", "Conciliación", "🏦"],
    ["beneficios.html", "Beneficios", "🎁"],
];

const $ = id => document.getElementById(id);

const esc = s => String(s ?? "").replace(/[&<>"']/g,
    c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));

const soles = n => "S/ " + Number(n ?? 0).toLocaleString("es-PE",
    { minimumFractionDigits: 2, maximumFractionDigits: 2 });

const formToObj = fd => Object.fromEntries(fd.entries());

async function api(method, url, body) {
    const opts = { method, headers: {} };
    if (body instanceof FormData) {
        opts.body = body;                       // el navegador pone el multipart
    } else if (body !== undefined) {
        opts.headers["Content-Type"] = "application/json";
        opts.body = JSON.stringify(body);
    }
    const res = await fetch(API + url, opts);
    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(data.error || `Error ${res.status}`);
    return data;
}

function toast(msg, ok = true) {
    const t = $("toast");
    t.textContent = msg;
    t.className = "toast show " + (ok ? "ok" : "error");
    clearTimeout(t._timer);
    t._timer = setTimeout(() => (t.className = "toast"), 3200);
}

function fillSelect(sel, items, label, placeholder) {
    sel.innerHTML = `<option value="">${placeholder}</option>` +
        items.map(i => `<option value="${i.id}">${esc(label(i))}</option>`).join("");
}

function badgeEstado(q) {
    if (q.vencida) return '<span class="badge err">Vencida</span>';
    const map = { PAGADA: "ok", PARCIAL: "warn", REFINANCIADA: "info", PENDIENTE: "" };
    return `<span class="badge ${map[q.estado] ?? ""}">${esc(q.estado)}</span>`;
}

function renderNav() {
    const actual = location.pathname.split("/").pop() || "index.html";
    $("nav").innerHTML = `<div class="brand">La Salle · Pensiones</div>` +
        NAV.map(([href, texto, icono]) =>
            `<a href="${href}" class="${href === actual ? "active" : ""}"><span>${icono}</span>${texto}</a>`
        ).join("");
}

document.addEventListener("DOMContentLoaded", renderNav);