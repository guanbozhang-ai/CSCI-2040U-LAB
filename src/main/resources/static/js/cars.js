fetch("cars.json")
    .then(response => response.json())
    .then(cars => {

        const table = document.querySelector("#carTable tbody");

        cars.forEach(car => {

            const row = document.createElement("tr");

            row.innerHTML = `
            <td>${car.make}</td>
            <td>${car.model}</td>
            <td>${car.bodyType}</td>
            <td>${car.horsepower}</td>
            <td>$${car.price}</td>
            <td>${car.mileage}</td>
            <td>${car.seating}</td>
        `;

            table.appendChild(row);

        });

    });