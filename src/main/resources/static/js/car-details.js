
const urlParams = new URLSearchParams(window.location.search);
const carId = urlParams.get('id');


fetch("cars.json")
    .then(response => response.json())
    .then(cars => {

        const car = cars.find((currentCar, index) => String(currentCar.id ?? index) === carId);

        if (car) {
            document.getElementById("carTitle").innerText = `${car.make} ${car.model}`;
            document.getElementById("carPrice").innerText = `$${car.price.toLocaleString()}`;
            document.getElementById("carBody").innerText = car.bodyType;
            document.getElementById("carMileage").innerText = `${car.mileage.toLocaleString()} mi`;
            document.getElementById("carHp").innerText = car.horsepower;
            document.getElementById("carSeats").innerText = car.seating;
        } else {
            document.getElementById("carTitle").innerText = "Car Not Found";
            document.querySelector(".specs-grid").style.display = "none";
        }
    })
    .catch(error => {
        console.error("Error fetching car details:", error);
        document.getElementById("carTitle").innerText = "Error loading details.";
    });
