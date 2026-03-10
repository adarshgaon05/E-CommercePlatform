// ================= REGISTER =================
function register() {

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!name || !email || !password) {
        alert("All fields are required!");
        return;
    }

    apiRequest("/auth/register", "POST", {
        name: name,
        email: email,
        password: password
    })
    .then(data => {

        if (data.token) {
            localStorage.setItem("token", data.token);
            alert("Registration successful!");
            window.location.href = "/index.html";
        } else {
            alert("Registration successful! Please login.");
            window.location.href = "/login.html";
        }

    })
    .catch(err => {
        alert("Registration failed! Email may already exist.");
        console.error(err);
    });
}


// ================= LOGIN =================
function login() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!email || !password) {
        alert("Email and Password required!");
        return;
    }

    apiRequest("/auth/login", "POST", {
        email: email,
        password: password
    })
    .then(data => {

        if (data.token) {
            localStorage.setItem("token", data.token);
            alert("Login successful!");
            window.location.href = "/index.html";
        } else {
            alert("Invalid credentials!");
        }

    })
    .catch(err => {
        alert("Login failed!");
        console.error(err);
    });
}


// ================= LOGOUT =================
function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}


// ================= GET TOKEN =================
function getToken() {
    return localStorage.getItem("token");
}


// ================= CHECK LOGIN =================
function isLoggedIn() {
    return !!getToken();
}


// ================= DECODE JWT =================
function decodeToken() {

    const token = getToken();
    if (!token) return null;

    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}


// ================= GET USER ROLE =================
function getUserRole() {

    const payload = decodeToken();
    if (!payload) return null;

    return payload.role || payload.authorities || null;
}


// ================= CHECK ADMIN =================
function isAdmin() {

    const role = getUserRole();

    if (!role) return false;

    if (typeof role === "string") {
        return role === "ROLE_ADMIN" || role === "ADMIN";
    }

    if (Array.isArray(role)) {
        return role.includes("ROLE_ADMIN");
    }

    return false;
}