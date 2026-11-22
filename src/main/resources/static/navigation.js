export function navBar() {
    let nav = document.createElement("nav");
    nav.className = "navbar p-3 gap-2";
    nav.style.backgroundColor = "#16191c";

    let home = document.createElement("button");
    home.innerText = "Home";
    home.className = "btn btn-outline-light";
    home.onclick = () => window.location.href = "/home";

    let gallery = document.createElement("button");
    gallery.innerText = "Gallery";
    gallery.className = "btn btn-outline-light";
    gallery.onclick = () => window.location.href = "/galleries/list";

    let dashboard = document.createElement("button");
    dashboard.innerText = "Dashboard";
    dashboard.className = "btn btn-outline-light";
    dashboard.onclick = () => window.location.href = "/app/dashboard";

    console.log("Appended DASH!")
    nav.append(home, gallery, dashboard);

    return nav;
}

document.body.prepend(navBar());
