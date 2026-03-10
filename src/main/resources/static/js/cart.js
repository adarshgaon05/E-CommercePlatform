// ================= GET CART =================
function getCart() {
    return JSON.parse(localStorage.getItem("cart")) || [];
}


// ================= SAVE CART =================
function saveCart(cart) {
    localStorage.setItem("cart", JSON.stringify(cart));
}


// ================= ADD TO CART =================
function addToCart(product) {

    let cart = getCart();

    cart.push(product);

    saveCart(cart);

    alert("Product added to cart!");
}


// ================= LOAD CART ITEMS =================
function loadCart() {

    const cart = getCart();
    const container = document.getElementById("cart-container");
    const totalElement = document.getElementById("total");

    if (!container) return;

    container.innerHTML = "";

    if (cart.length === 0) {
        container.innerHTML = "<p>Your cart is empty.</p>";
        if (totalElement) totalElement.innerText = "";
        return;
    }

    let total = 0;

    cart.forEach((item, index) => {

        total += parseFloat(item.price);

        const div = document.createElement("div");
        div.classList.add("product-card");

        div.innerHTML = `
            <h3>${item.name}</h3>
            <p>${item.description}</p>
            <strong>₹ ${item.price}</strong><br><br>
            <button onclick="removeFromCart(${index})">
                Remove
            </button>
        `;

        container.appendChild(div);
    });

    if (totalElement) {
        totalElement.innerText = "Total: ₹ " + total;
    }
}


// ================= REMOVE ITEM =================
function removeFromCart(index) {

    let cart = getCart();

    cart.splice(index, 1);

    saveCart(cart);

    loadCart();
}


// ================= CHECKOUT =================
function checkout() {

    const cart = getCart();

    if (cart.length === 0) {
        alert("Cart is empty!");
        return;
    }

    alert("Order placed successfully!");

    localStorage.removeItem("cart");

    loadCart();
}