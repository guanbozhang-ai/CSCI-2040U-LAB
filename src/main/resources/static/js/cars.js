const tableBody = document.querySelector("#carTable tbody");
const makeSelect = document.getElementById("make");
const bodyTypeSelect = document.getElementById("bodyType");
const maxPriceInput = document.getElementById("maxPrice");
const applyFiltersButton = document.getElementById("applyFilters");
let allCars = [];

function renderCars(cars) {
    tableBody.innerHTML = "";

    if (cars.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="7">No cars match your filters.</td>`;
        tableBody.appendChild(row);
        return;
    }

    cars.forEach((car, index) => {
        const row = document.createElement("tr");
        const carKey = car.id ?? index;

        row.innerHTML = `
            <td>${car.make}</td>
            <td>${car.model}</td>
            <td>${car.bodyType}</td>
            <td>${car.horsepower}</td>
            <td>$${Number(car.price).toLocaleString()}</td>
            <td>${Number(car.mileage).toLocaleString()}</td>
            <td>${car.seating}</td>
        `;

        row.addEventListener("click", () => {
            window.location.href = `car-details.html?id=${encodeURIComponent(carKey)}`;
        });

        tableBody.appendChild(row);
    });
}

function showError(message) {
    tableBody.innerHTML = "";
    const row = document.createElement("tr");
    row.innerHTML = `<td colspan="7">${message}</td>`;
    tableBody.appendChild(row);
}

function populateSelect(selectElement, values, allLabel) {
    selectElement.innerHTML = "";

    const allOption = document.createElement("option");
    allOption.value = "";
    allOption.textContent = allLabel;
    selectElement.appendChild(allOption);

    values.forEach(value => {
        const option = document.createElement("option");
        option.value = value.toLowerCase();
        option.textContent = value;
        selectElement.appendChild(option);
    });
}

// ✅ Client-side filtering — no backend needed
function applyFilters(filters) {
    return allCars.filter(car => {
        if (filters.make && car.make.toLowerCase() !== filters.make.toLowerCase()) return false;
        if (filters.bodyType && car.bodyType.toLowerCase() !== filters.bodyType.toLowerCase()) return false;
        if (filters.maxPrice && Number(car.price) > Number(filters.maxPrice)) return false;
        return true;
    });
}

async function initializeCarsPage() {
    try {
        const response = await fetch("cars.json");
        if (!response.ok) {
            throw new Error(`Failed to fetch stock: ${response.status}`);
        }

        allCars = await response.json();
        const makes = [...new Set(allCars.map(car => car.make))].sort();
        const bodyTypes = [...new Set(allCars.map(car => car.bodyType))].sort();

        populateSelect(makeSelect, makes, "All Makes");
        populateSelect(bodyTypeSelect, bodyTypes, "All Types");
        renderCars(allCars);
    } catch (error) {
        console.error("Error fetching car list:", error);
        showError("Unable to load cars in stock.");
    }
}

applyFiltersButton.addEventListener("click", () => {
    try {
        const filters = {
            make: makeSelect.value.trim(),
            bodyType: bodyTypeSelect.value.trim(),
            maxPrice: maxPriceInput.value.trim()
        };

        const hasFilters = Object.values(filters).some(value => value !== "");
        if (!hasFilters) {
            renderCars(allCars);
            return;
        }

        const filteredCars = applyFilters(filters);
        renderCars(filteredCars);
    } catch (error) {
        console.error("Error applying filters:", error);
        showError("Unable to apply filters.");
    }
});

initializeCarsPage();
