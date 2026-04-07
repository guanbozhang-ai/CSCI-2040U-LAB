document.addEventListener("DOMContentLoaded", () => {

    populateSurveyOptions();

    document.getElementById("surveyForm").addEventListener("submit", handleSubmit);
});


// ==========================
//read car.json get select
// ==========================
async function populateSurveyOptions() {

    try {
        const response = await fetch("cars.json");
        const cars = await response.json();

        const makes = [...new Set(cars.map(c => c.make))].sort();
        const bodyTypes = [...new Set(cars.map(c => c.bodyType))].sort();
        const fuelTypes = [...new Set(cars.map(c => c.fuelType))].sort();
        const transmissions = [...new Set(cars.map(c => c.transmission))].sort();

        fillSelect("makes", makes, true);
        fillSelect("bodyType", bodyTypes, true);
        fillSelect("fuelType", fuelTypes, false);
        fillSelect("transmission", transmissions, false);

    } catch (err) {
        console.error("Failed to load options:", err);
    }
}


// ==========================
//fill select
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
// send to api
// ==========================
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

        const surveyData = {
            price: getNumber("price"),
            priceImportance: getNumber("priceImportance"),

            horsepower: getNumber("horsepower"),
            powerImportance: getNumber("powerImportance"),

            mileage: getNumber("mileage"),
            mileageImportance: getNumber("mileageImportance"),

            seats: getNumber("seats"),
            seatImportance: getNumber("seatImportance"),

            economy: getNumber("economy"),
            economyImportance: getNumber("economyImportance"),

            year: getNumber("year"),
            yearImportance: getNumber("yearImportance"),

            makes: [formData.get("makes")],
            bodyType: formData.get("bodyType"),

            fuelType: formData.get("fuelType"),
            transmission: formData.get("transmission")
        };

        console.log("Sending:", surveyData);

        fetch("http://localhost:8080/api/match", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(surveyData)
        })
            .then(res => res.json())
            .then(result => {

                const tbody = document.querySelector("#resultTable tbody");
                tbody.innerHTML = "";


                result.forEach(car => {

                    const row = document.createElement("tr");

                    row.innerHTML = `
            <td>${car.make}</td>
            <td>${car.model}</td>
            <td>${car.bodyType}</td>
            <td>${car.horsepower}</td>
            <td>$${Number(car.price).toLocaleString()}</td>
            <td>${car.mileage}</td>
            <td>${car.seating}</td>
        `;

                    // ✅ 点击跳详情页
                    row.addEventListener("click", () => {
                        window.location.href = `car-details.html?id=${car.id}`;
                    });

                    tbody.appendChild(row);
                });
            })

            .catch(err => {
                console.error(err);
                alert("Error connecting to server");
            });

    } catch (err) {
        alert(err.message);
    }
}