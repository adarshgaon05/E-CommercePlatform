// ================= BASE URL =================
const BASE_URL = "http://localhost:8080/api";


// ================= GET TOKEN =================
function getToken() {
    return localStorage.getItem("token");
}


// ================= LOGOUT IF UNAUTHORIZED =================
function handleUnauthorized(status) {
    if (status === 401 || status === 403) {
        alert("Session expired or unauthorized. Please login again.");
        localStorage.removeItem("token");
        window.location.href = "/login.html";
    }
}


// ================= COMMON API REQUEST =================
async function apiRequest(endpoint, method = "GET", body = null) {

    const headers = {
        "Content-Type": "application/json"
    };

    // Automatically attach token if exists
    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const options = {
        method: method,
        headers: headers
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(BASE_URL + endpoint, options);

        // Handle unauthorized
        if (response.status === 401 || response.status === 403) {
            handleUnauthorized(response.status);
            throw new Error("Unauthorized");
        }

        if (!response.ok) {
            throw new Error("API Error: " + response.status);
        }

        // Handle empty response
        const contentType = response.headers.get("content-type");

        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        } else {
            return await response.text();
        }

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}