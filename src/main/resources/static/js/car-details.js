
const urlParams = new URLSearchParams(window.location.search);
const carId = urlParams.get('id');


fetch("cars.json")
    .then(response => response.json())
    .then(cars => {

        const car = cars.find((currentCar, index) => String(currentCar.id ?? index) === carId);

        if (car) {
            document.getElementById("carTitle").innerText = `${car.year} ${car.make} ${car.model} - $${car.price.toLocaleString()}`;
            document.getElementById("carBody").innerText = car.bodyType;
            document.getElementById("carMileage").innerText = `${car.mileage.toLocaleString()} km`;
            document.getElementById("carHp").innerText = car.horsepower;
            document.getElementById("carSeats").innerText = car.seating;
            document.getElementById("carDrivetrain").innerText = car.drivetrainConfiguration;
            document.getElementById("carEngine").innerText = `${car.transmission} ${car.cylinders}-cylinder, ${car.engineConfiguration} Configuration`;
            document.getElementById("carExtColour").innerText = car.extColour;
            document.getElementById("carIntColour").innerText = car.intColour;

        } else {
            document.getElementById("carTitle").innerText = "Car Not Found";
            document.querySelector(".specs-grid").style.display = "none";
        }
    })
    .catch(error => {
        console.error("Error fetching car details:", error);
        document.getElementById("carTitle").innerText = "Error loading details.";
    });
