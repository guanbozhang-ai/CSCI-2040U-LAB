document.addEventListener("DOMContentLoaded", () => {
    populateSurveyOptions();
    document.getElementById("surveyForm").addEventListener("submit", handleSubmit);
});


// ==========================
// Read cars.json and populate selects
// ==========================
async function populateSurveyOptions() {
    try {
        const response = await fetch("cars.json");
        const cars = await response.json();

        const makes = [...new Set(cars.map(c => c.make))].sort();
        const bodyTypes = [...new Set(cars.map(c => c.bodyType))].sort();
        const fuelTypes = [...new Set(cars.map(c => c.fuelType))].sort();
        const transmissions = [...new Set(cars.map(c => c.transmission))].sort();

        fillSelect("makes", makes, false);
        fillSelect("bodyType", bodyTypes, true);
        fillSelect("fuelType", fuelTypes, false);
        fillSelect("transmission", transmissions, false);

    } catch (err) {
        console.error("Failed to load options:", err);
    }
}


// ==========================
// Fill a <select> element
// ==========================
function fillSelect(id, values, required) {
    const select = document.getElementById(id);
    select.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = required ? "Select" : "No preference";
    select.appendChild(defaultOption);

    values.forEach(v => {
        const option = document.createElement("option");
        option.value = v;
        option.textContent = v;
        select.appendChild(option);
    });
}


// ==========================
// ✅ Client-side smart match scoring — no backend needed
// ==========================
function scoreCar(car, prefs) {
    let score = 0;

    // Hard filters — knock out cars that don't match categorical preferences
    if (prefs.makes && prefs.makes[0] && car.make !== prefs.makes[0]) return null;
    if (prefs.bodyType && prefs.bodyType !== "" && car.bodyType !== prefs.bodyType) return null;
    if (prefs.fuelType && prefs.fuelType !== "" && car.fuelType !== prefs.fuelType) return null;
    if (prefs.transmission && prefs.transmission !== "" && car.transmission !== prefs.transmission) return null;

    // Scored dimensions — closer to ideal = higher score (max 100 per dimension)
    function dimensionScore(carVal, idealVal, importance, maxDelta) {
        const delta = Math.abs(carVal - idealVal);
        const normalized = Math.max(0, 1 - delta / maxDelta);
        return normalized * importance * 100;
    }

    score += dimensionScore(car.price,       prefs.price,       prefs.priceImportance,    50000);
    score += dimensionScore(car.horsepower,  prefs.horsepower,  prefs.powerImportance,    400);
    score += dimensionScore(car.mileage,     prefs.mileage,     prefs.mileageImportance,  150000);
    score += dimensionScore(car.seating,     prefs.seats,       prefs.seatImportance,     8);
    score += dimensionScore(car.fuelEconomy, prefs.economy,     prefs.economyImportance,  20);
    score += dimensionScore(car.year,        prefs.year,        prefs.yearImportance,     10);

    return { ...car, score: Math.round(score) };
}

function handleSubmit(e) {
    e.preventDefault();

    const formData = new FormData(e.target);

    function getNumber(name) {
        const val = Number(formData.get(name));
        if (isNaN(val) || val <= 0) {
            throw new Error(`${name} is invalid`);
        }
        return val;
    }

    try {
        const prefs = {
            price:             getNumber("price"),
            priceImportance:   getNumber("priceImportance"),

            horsepower:        getNumber("horsepower"),
            powerImportance:   getNumber("powerImportance"),

            mileage:           getNumber("mileage"),
            mileageImportance: getNumber("mileageImportance"),

            seats:             getNumber("seats"),
            seatImportance:    getNumber("seatImportance"),

            economy:           getNumber("economy"),
            economyImportance: getNumber("economyImportance"),

            year:              getNumber("year"),
            yearImportance:    getNumber("yearImportance"),

            makes:        [formData.get("makes")],
            bodyType:     formData.get("bodyType"),
            fuelType:     formData.get("fuelType"),
            transmission: formData.get("transmission")
        };

        console.log("Preferences:", prefs);

        // Load cars and score them locally
        fetch("cars.json")
            .then(res => res.json())
            .then(cars => {
                const scored = cars
                    .map(car => scoreCar(car, prefs))
                    .filter(car => car !== null)
                    .sort((a, b) => b.score - a.score);

                const tbody = document.querySelector("#resultTable tbody");
                tbody.innerHTML = "";

                if (scored.length === 0) {
                    const row = document.createElement("tr");
                    row.innerHTML = `<td colspan="7">No matching cars found. Try relaxing your preferences.</td>`;
                    tbody.appendChild(row);
                    return;
                }

                scored.forEach(car => {
                    const row = document.createElement("tr");
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
                        window.location.href = `car-details.html?id=${car.id}`;
                    });

                    tbody.appendChild(row);
                });
            })
            .catch(err => {
                console.error(err);
                alert("Error loading car data.");
            });

    } catch (err) {
        alert(err.message);
    }
}