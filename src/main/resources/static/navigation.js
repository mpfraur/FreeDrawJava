

export function navBar() {
    let nav = document.createElement("nav");

    let home = document.createElement("button");
    home.innerText = "Home";
    home.onclick = () => window.location.href = "/home";

    let gallery = document.createElement("button");
    gallery.innerText = "Gallery";
    gallery.onclick = () => window.location.href = "/galleries/list";

    nav.append(home, gallery);

    return nav;
}
document.body.prepend(navBar());
