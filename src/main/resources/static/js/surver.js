
document.getElementById("surveyForm").addEventListener("submit", function(e){

    e.preventDefault()

    const formData = new FormData(this)

    const surveyData = {
        price: Number(formData.get("price")),
        priceImportance: Number(formData.get("priceImportance")),
        horsepower: Number(formData.get("horsepower")),
        powerImportance: Number(formData.get("powerImportance")),
        mileage: Number(formData.get("mileage")),
        mileageImportance: Number(formData.get("mileageImportance")),
        seats: Number(formData.get("seats")),
        seatImportance: Number(formData.get("seatImportance")),
        makes: [formData.get("makes")],
        bodyType: formData.get("bodyType")
    }

    fetch("http://localhost:8080/api/match", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(surveyData)
    })
        .then(res => res.json())
        .then(result => {

            console.log("Match result:", result)

            document.getElementById("result").innerHTML = `
        <h2>Best Match:</h2>
        <p>${result.make} ${result.model}</p>
        <p>Price: $${result.price}</p>
        <p>Horsepower: ${result.horsepower}</p>
    `;
        })
        .catch(err => {
            console.error(err)
            alert("Error connecting to server")
        })

    showResult(bestCar);
});
