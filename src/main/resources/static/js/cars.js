const tableBody = document.querySelector("#carTable tbody");
const makeSelect = document.getElementById("make");
const bodyTypeSelect = document.getElementById("bodyType");
const maxPriceInput = document.getElementById("maxPrice");
const applyFiltersButton = document.getElementById("applyFilters");

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

fetch("cars.json")
    .then(response => response.json())
    .then(cars => {
        const makes = [...new Set(cars.map(car => car.make))].sort();
        const bodyTypes = [...new Set(cars.map(car => car.bodyType))].sort();

        populateSelect(makeSelect, makes, "All Makes");
        populateSelect(bodyTypeSelect, bodyTypes, "All Types");
        renderCars(cars);

        applyFiltersButton.addEventListener("click", () => {
            const selectedMake = makeSelect.value.trim().toLowerCase();
            const selectedBodyType = bodyTypeSelect.value.trim().toLowerCase();
            const maxPrice = Number(maxPriceInput.value);

            const filteredCars = cars.filter(car => {
                const matchesMake = !selectedMake || car.make.toLowerCase() === selectedMake;
                const matchesBodyType = !selectedBodyType || car.bodyType.toLowerCase() === selectedBodyType;
                const matchesPrice = !maxPriceInput.value || Number(car.price) <= maxPrice;

                return matchesMake && matchesBodyType && matchesPrice;
            });

            renderCars(filteredCars);
        });
    })
    .catch(error => {
        console.error("Error fetching car list:", error);
    });
