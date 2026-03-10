// ================= LOAD PRODUCTS =================
function loadProducts() {

    apiRequest("/products")
        .then(products => {

            const container = document.getElementById("product-list");

            if (!container) return;

            container.innerHTML = "";

            if (products.length === 0) {
                container.innerHTML = "<p>No products available.</p>";
                return;
            }

            products.forEach(product => {

                const div = document.createElement("div");
                div.classList.add("product-card");

                div.innerHTML = `
                    <h3>${product.name}</h3>
                    <p>${product.description}</p>
                    <strong>₹ ${product.price}</strong><br><br>
                    <button onclick='addToCart(${JSON.stringify(product)})'>
                        Add to Cart
                    </button>
                `;

                container.appendChild(div);
            });

        })
        .catch(error => {
            console.error("Error loading products:", error);
        });
}


// ================= AUTO LOAD WHEN PAGE OPENS =================
document.addEventListener("DOMContentLoaded", loadProducts);