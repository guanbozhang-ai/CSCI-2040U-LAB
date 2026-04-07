
const surveyForm = document.getElementById("surveyForm");
const resultDiv = document.getElementById("result");

surveyForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new FormData(this);
    const preferredMake = formData.get("makes");

    const surveyData = {
        price: Number(formData.get("price")),
        priceImportance: Number(formData.get("priceImportance")),
        horsepower: Number(formData.get("horsepower")),
        powerImportance: Number(formData.get("powerImportance")),
        mileage: Number(formData.get("mileage")),
        mileageImportance: Number(formData.get("mileageImportance")),
        seats: Number(formData.get("seats")),
        seatImportance: Number(formData.get("seatImportance")),
        makes: preferredMake ? [preferredMake] : [],
        bodyType: formData.get("bodyType")
    };

    resultDiv.style.display = "block";
    resultDiv.classList.remove("is-clickable");
    resultDiv.innerHTML = "<strong>Processing...</strong> matching your preferences with our inventory.";
    resultDiv.onclick = null;

    fetch("http://localhost:8080/api/match", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(surveyData)
    })
        .then((res) => {
            if (!res.ok) {
                throw new Error("Failed to fetch car match");
            }
            return res.json();
        })
        .then((result) => {
            resultDiv.innerHTML = `
                <h2>Best Match:</h2>
                <p>${result.make} ${result.model}</p>
                <p>Price: $${result.price}</p>
                <p>Horsepower: ${result.horsepower}</p>
            `;

            if (result.id !== undefined && result.id !== null) {
                resultDiv.classList.add("is-clickable");
                resultDiv.onclick = () => {
                    window.location.href = `car-details.html?id=${encodeURIComponent(result.id)}`;
                };
            }
        })
        .catch((err) => {
            console.error(err);
            resultDiv.innerHTML = '<strong>Error.</strong> We could not match a car right now. Please try again.';
        });
});
