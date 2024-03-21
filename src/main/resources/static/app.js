const rootElement = document.documentElement;
const theme = setDarkMode();

document.addEventListener("DOMContentLoaded", function () {
    const el = document.querySelector(".fadeoutMessage");
    if (el) {
        setTimeout(() => fadeout(el), 3000);
    }

    initDarkModeButtons(theme);
});

function setDarkMode() {
    const theme = localStorage.getItem("theme");

    theme && rootElement.classList.add(theme);

    return theme;
}

function initDarkModeButtons(theme) {
    const toggleButton = document.getElementById("toggleTheme");

    toggleButton?.addEventListener("click", function() {
        rootElement.classList.toggle("dark");

        if (rootElement.classList.contains("dark")) {
            localStorage.setItem("theme", "dark");
        } else {
            localStorage.removeItem("theme");
        }
    });
}

function fadeout(el) {
    if (el !== null) {
        el.classList.add("fade-out");
        setTimeout(() => {
            el.remove();
        }, 1000);
    }
}

function handleDelete(event) {
    event.preventDefault();
    const form = event.target;
    const data = new FormData(form);
    const url = form.action;
    const popupMessage = document.querySelector("#redFeedback");

    fetch(url, {
        method: 'POST',
        body: data
    }).then(resp => {
        if (resp.redirected && resp.url.indexOf("login") > -1) { // session expired
            window.location.reload();
        }

        form.parentNode.remove();
    });

    popupMessage.className = "fadeoutMessage";
    setTimeout(() => {
        popupMessage.className = "hidden";
    }, 3000);
}

function focusAdd() {
    document.querySelector("#add").focus({preventScroll: true});
}

function updateWeekProgressbar() {
    const progressbar = document.querySelector(".week-progressbar");
    const now = new Date();
    const weekProgress = now.getDay() === 0 ? 100 : ((now.getDay() - 1) / 6 + now.getHours() / 24 / 6) * 100;

    if (weekProgress === 100) {
        progressbar.classList.add("sunday");
    }

    progressbar.style.width = weekProgress + "%";
}

function expandMatureTasks() {
    const TASK_MAX_PRIORITY = 4;
    const tasks = document.querySelectorAll(".task");
    tasks.forEach(task => {
        const size = task.dataset.maturity / 10 / (TASK_MAX_PRIORITY - task.dataset.priority + 1) + "rem";
        task.style.paddingTop = size;
        task.style.paddingBottom = size;
    });
}

function handleSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const data = new FormData(form);
    const url = form.action;

    fetch(url, {
        method: 'POST',
        body: data
    }).then(resp => {
        if (resp.redirected && resp.url.indexOf("login") > -1) { // session expired
            window.location.reload();
        }

        const done = form.elements["done"];
        done.value = done.value === "false" ? "true" : "false";

        const task = form.closest(".task");
        task.dataset.done = task.dataset.done === "true" ? "false" : "true";
    });
}

function filterTasks(filter) {
    const tasks = document.querySelectorAll('.taskContainer');
    const filterText = document.querySelector("#filter-text");

    localStorage.setItem('filter', filter);

    if (filter === 'all') {
        localStorage.removeItem('filter');
        if (filterText.dataset.text !== undefined) {
            filterText.innerText = filterText.dataset.text;
        }
    } else {
        if (filterText.dataset.text === undefined) {
            filterText.dataset.text = filterText.innerText;
        }
        filterText.innerText = filter;
    }

    const date = new Date();

    const today = date.toISOString().slice(0, 10);

    date.setDate(date.getDate() + 1);
    const tomorrow = date.toISOString().slice(0, 10);

    tasks.forEach((task) => {
        task.style.display = "";
        const dataset = task.querySelector(".task").dataset;

        const taskDone = dataset.done === "true";
        if (filter === 'not done' && taskDone ||
            filter === 'current' && (dataset.duedate < today || taskDone) ||
            filter === 'tomorrow' && (dataset.duedate !== tomorrow || taskDone)) {

            task.style.display = "none";
        }
    });
}

function showCalendar() {
    const date = document.querySelector("#date");
    const dateLabel = document.querySelector("label.dateTime")

    dateLabel.dispatchEvent(new Event("click"));
    if (document.activeElement !== date) {
        date.focus();
        date.showPicker();
    }
}

function toggleDueDate(forceShow) {
    const CLASS = "hidden";
    const dateWrapper = document.querySelector("div.dateTime")
    const classList = dateWrapper.classList;

    focusAdd();

    if (forceShow === undefined) {
        classList.toggle(CLASS);
        due(null);
    } else if (forceShow === true) {
        classList.remove(CLASS);
    } else {
        classList.add(CLASS);
        due(null);
    }
}

function initTaskEditListener() {
    document.querySelector("main").addEventListener("click", function (event) {
        if (event.target.tagName !== "P") return;

        let task = event.target.closest(".task");

        if (!task) return;

        edit(task);
    });
}

function edit(task) {
    document.querySelector("div.hidden").classList.remove("hidden");
    document.querySelector("#add").focus();

    const main = document.querySelector("main");
    registerCancel(main);

    const description = task.querySelector("p").innerText;
    const dueDate = task.querySelector(".due").dataset.duedate;
    const priority = task.dataset.priority;

    const form = document.querySelector("footer form");
    setPriority(priority);

    form.action = "/tasks/" + task.dataset.id;
    form.querySelector("input[name='description']").value = description;
    form.querySelector("input[name='maybeDueDate']").value = dueDate;

    function registerCancel(el) {
        el.classList.add("fade");

        el.addEventListener("click", function () {
            cancelEdit();
        }, {once: true});

        window.onkeydown = function (event) {
            if (event.key === "Escape") {
                cancelEdit();
            }
        }

        function cancelEdit() {
            location.reload();
        }
    }
}

function setPriority(priority) {
    document.querySelector("button[name='priority']").value = priority;

    const buttons = document.querySelectorAll("footer .stars button");
    buttons.forEach(button => {
        if (button.value == priority) {
            button.classList.add("active");
        } else {
            button.classList.remove("active");
        }
    });
}

function initPhoneHack() {
    window.visualViewport.addEventListener('resize', handleKeyboard);
    document.addEventListener('focusin', handleKeyboard);
    document.addEventListener('focusout', handleKeyboard);

    function handleKeyboard() {
        requestAnimationFrame(() => {
            window.scrollY > 0 ? adjustSafeAreaInset(0) : adjustSafeAreaInset();

            [4, 40, 400].forEach((ms) => {
                setTimeout(() => {
                    scrollToBottom();
                }, ms);
            })
        });
    }

    function adjustSafeAreaInset(value = 'calc(env(safe-area-inset-bottom)/2)') {
        document.querySelector("footer").style.paddingBottom = value;
    }
}

function scrollToBottom() {
    document.querySelector(".stars").scrollIntoView({behavior: "instant"});
}

function toggleDropdown(target) {
    popOverActive = !popOverActive;

    const dropdownOptions = document.querySelector(".dropdown-content");
    const filtersWrapper = document.querySelector(".filters");
    const textSpan = document.querySelector("#filter-text");
    const menuOpenIcon = document.querySelector("#menu_open");
    const menuCloseIcon = document.querySelector("#menu_close");
    const main = document.querySelector("main");
    const footer = document.querySelector("footer");


    registerCancel(main);
    registerCancel(footer);

    if (target === 'dropdownOptions') {
        dropdownOptions.style.display = (dropdownOptions.style.display === "block") ? "none" : "block";
        if (filtersWrapper.style.display === 'flex') {
            filtersWrapper.style.display = 'none';
        }
        if (dropdownOptions.style.display === "block") {
            menuOpenIcon.style.display = "none";
            menuCloseIcon.style.display = "inline-block";
        } else {
            menuOpenIcon.style.display = "inline-block";
            menuCloseIcon.style.display = "none";
        }
    } else if (target === 'filters') {
        filtersWrapper.style.display = (filtersWrapper.style.display === 'flex') ? "none" : "flex";

        if (dropdownOptions.style.display === "block") {
            dropdownOptions.style.display = "none";
            menuOpenIcon.style.display = "inline-block";
            menuCloseIcon.style.display = "none";
        }
    }

    function registerCancel(element) {
        element.addEventListener("click", function () {
            cancelMenu();
        }, {once: true});

        window.onkeydown = function (event) {
            if (event.key === "Escape") {
                cancelMenu();
            }
        }

        function cancelMenu() {
            popOverActive = false;
            dropdownOptions.style.display = "none";
            filtersWrapper.style.display = 'none';
            menuOpenIcon.style.display = "inline-block";
            menuCloseIcon.style.display = "none";
        }
    }
}


function logout() {
    document.querySelector('#logoutForm').submit();
}

function dueNextWeek() {
    due(7);
}

function dueThisWeek() {
    const days = [3, 3, 2, 2, 2, 1, 0][new Date().getDay()];
    due(days);
}

function dueTomorrow() {
    due(1);
}

function dueAt(date) {
    const days = Math.ceil((new Date(date) - new Date()) / (1000 * 60 * 60 * 24));
    due(days);
}

function due(days) {
    const dateEl = document.querySelector("#date");

    focusAdd();

    if (days === null) {
        dateEl.value = "";
    } else {
        const date = new Date();
        date.setDate(date.getDate() + days);
        dateEl.valueAsDate = date;
    }

    dateEl.dispatchEvent(new Event("input"));
}

function urlB64ToUint8Array(base64String) {
    const padding = '='.repeat((4 - base64String.length % 4) % 4);
    const base64 = (base64String + padding)
        .replace(/\-/g, '+')
        .replace(/_/g, '/');

    const rawData = window.atob(base64);
    const outputArray = new Uint8Array(rawData.length);

    for (let i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }

    return outputArray;
}

function redirectToDiscord() {
    let discordUrl = 'https://discord.gg/cP9Xa5TcuX';
    window.open(discordUrl, '_blank');
}

function togglePasswordVisibility() {
    const password = document.querySelector("#password");
    const closeEye = document.querySelector(".showPassword");
    const openEye = document.querySelector(".hidePassword");

    if (password.type === "password") {
        password.type = "text";
        closeEye.style.display = "none";
        openEye.style.display = "inline-block";
    }
    else {
        password.type = "password";
        closeEye.style.display = "inline-block";
        openEye.style.display = "none";
    }
}