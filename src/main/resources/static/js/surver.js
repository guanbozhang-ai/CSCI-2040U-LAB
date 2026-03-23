const surveyForm = document.getElementById("surveyForm");
const makeSelect = document.getElementById("makes");
const bodyTypeSelect = document.getElementById("bodyType");
const resultMessage = document.getElementById("resultMessage");
const resultCard = document.getElementById("resultCard");
const resultTitle = document.getElementById("resultTitle");
const resultPrice = document.getElementById("resultPrice");
const resultYear = document.getElementById("resultYear");
const resultBodyType = document.getElementById("resultBodyType");
const resultMileage = document.getElementById("resultMileage");
const resultHorsepower = document.getElementById("resultHorsepower");
const resultSeating = document.getElementById("resultSeating");
const resultTransmission = document.getElementById("resultTransmission");
const resultLink = document.getElementById("resultLink");

let cars = [];

function addOptions(selectElement, values, defaultLabel) {
    selectElement.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = defaultLabel;
    selectElement.appendChild(defaultOption);

    values.forEach(value => {
        const option = document.createElement("option");
        option.value = value;
        option.textContent = value;
        selectElement.appendChild(option);
    });
}

function getNumberValue(formData, key) {
    const value = formData.get(key);
    return value === "" ? null : Number(value);
}

function clampImportance(value) {
    if (value === null || Number.isNaN(value)) {
        return 0;
    }

    return Math.max(0, Math.min(5, value));
}

function scoreCar(car, preferences) {
    let score = 0;

    if (preferences.make && car.make === preferences.make) {
        score += 30;
    }

    if (preferences.bodyType && car.bodyType === preferences.bodyType) {
        score += 20;
    }

    if (preferences.price !== null) {
        const difference = Math.abs(Number(car.price) - preferences.price);
        const priceScore = Math.max(0, 100 - difference / 300);
        score += priceScore * (clampImportance(preferences.priceImportance) + 1);
    }

    if (preferences.horsepower !== null) {
        const difference = Math.abs(Number(car.horsepower) - preferences.horsepower);
        const horsepowerScore = Math.max(0, 100 - difference / 3);
        score += horsepowerScore * (clampImportance(preferences.powerImportance) + 1);
    }

    if (preferences.mileage !== null) {
        const difference = Math.max(0, Number(car.mileage) - preferences.mileage);
        const mileageScore = Math.max(0, 100 - difference / 500);
        score += mileageScore * (clampImportance(preferences.mileageImportance) + 1);
    }

    if (preferences.seats !== null) {
        const difference = Math.abs(Number(car.seating) - preferences.seats);
        const seatScore = Math.max(0, 100 - difference * 25);
        score += seatScore * (clampImportance(preferences.seatImportance) + 1);
    }

    return score;
}

function showResult(car) {
    resultMessage.textContent = "Best available match based on your survey answers:";
    resultCard.hidden = false;
    resultTitle.textContent = `${car.make} ${car.model}`;
    resultPrice.textContent = `$${Number(car.price).toLocaleString()}`;
    resultYear.textContent = car.year;
    resultBodyType.textContent = car.bodyType;
    resultMileage.textContent = `${Number(car.mileage).toLocaleString()} mi`;
    resultHorsepower.textContent = car.horsepower;
    resultSeating.textContent = car.seating;
    resultTransmission.textContent = car.transmission;
    resultLink.href = `car-details.html?id=${encodeURIComponent(car.id)}`;
}

fetch("cars.json")
    .then(response => response.json())
    .then(data => {
        cars = data;

        const makes = [...new Set(cars.map(car => car.make))].sort();
        const bodyTypes = [...new Set(cars.map(car => car.bodyType))].sort();

        addOptions(makeSelect, makes, "Any make");
        addOptions(bodyTypeSelect, bodyTypes, "Any body type");
    })
    .catch(error => {
        console.error("Error loading survey data:", error);
        resultMessage.textContent = "Unable to load inventory for matching right now.";
    });

surveyForm.addEventListener("submit", event => {
    event.preventDefault();

    if (cars.length === 0) {
        resultMessage.textContent = "No inventory is available for matching right now.";
        resultCard.hidden = true;
        return;
    }

    const formData = new FormData(surveyForm);
    const preferences = {
        price: getNumberValue(formData, "price"),
        priceImportance: getNumberValue(formData, "priceImportance"),
        horsepower: getNumberValue(formData, "horsepower"),
        powerImportance: getNumberValue(formData, "powerImportance"),
        mileage: getNumberValue(formData, "mileage"),
        mileageImportance: getNumberValue(formData, "mileageImportance"),
        seats: getNumberValue(formData, "seats"),
        seatImportance: getNumberValue(formData, "seatImportance"),
        make: formData.get("makes"),
        bodyType: formData.get("bodyType")
    };

    const bestCar = cars
        .map(car => ({ car, score: scoreCar(car, preferences) }))
        .sort((left, right) => right.score - left.score)[0].car;

    showResult(bestCar);
});
