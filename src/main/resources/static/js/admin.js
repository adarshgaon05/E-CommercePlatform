const API = "http://localhost:8080/api";

// ================= CHECK LOGIN =================
function checkAdminLogin() {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first!");
        window.location.href = "/login.html";
        return;
    }
}

checkAdminLogin();


// ================= LOGOUT =================
function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}


// ================= ADD PRODUCT =================
function addProduct() {

    const token = localStorage.getItem("token");

    const name = document.getElementById("name").value;
    const description = document.getElementById("description").value;
    const price = document.getElementById("price").value;

    if (!name || !description || !price) {
        alert("All fields are required!");
        return;
    }

    fetch(API + "/products", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            name: name,
            description: description,
            price: price
        })
    })
    .then(res => {
        if (res.status === 403) {
            alert("Access Denied! Admin Only.");
            return null;
        }
        return res.json();
    })
    .then(data => {
        if (data) {
            alert("Product Added Successfully!");
            clearForm();
            loadProducts();
        }
    })
    .catch(err => console.error("Error:", err));
}


// ================= LOAD PRODUCTS =================
function loadProducts() {

    fetch(API + "/products")
    .then(res => res.json())
    .then(data => {

        const container = document.getElementById("product-list");
        container.innerHTML = "";

        data.forEach(product => {

            const div = document.createElement("div");
            div.classList.add("product-card");

            div.innerHTML = `
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <strong>₹ ${product.price}</strong><br><br>
                <button onclick="deleteProduct(${product.id})">
                    Delete
                </button>
            `;

            container.appendChild(div);
        });

    })
    .catch(err => console.error("Error:", err));
}


// ================= DELETE PRODUCT =================
function deleteProduct(id) {

    const token = localStorage.getItem("token");

    fetch(API + "/products/" + id, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (res.status === 403) {
            alert("Only ADMIN can delete!");
            return;
        }
        alert("Product Deleted Successfully!");
        loadProducts();
    })
    .catch(err => console.error("Error:", err));
}


// ================= CLEAR FORM =================
function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("description").value = "";
    document.getElementById("price").value = "";
}


// Auto load products when page opens
loadProducts();