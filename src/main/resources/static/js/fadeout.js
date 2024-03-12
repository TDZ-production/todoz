const el = document.querySelector(".fadeoutMessage");
if (el) {
    setTimeout(() => fadeout(el), 3000);
}

function fadeout(el) {
    if (el !== null) {
        el.classList.add("fade-out");
        setTimeout(() => {
            el.remove();
        }, 1000);
    }
}