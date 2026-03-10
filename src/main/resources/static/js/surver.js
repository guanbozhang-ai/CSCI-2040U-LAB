document.getElementById("surveyForm").addEventListener("submit", function(e){

    e.preventDefault()

    const formData = new FormData(this)

    const surveyData = {
        price: formData.get("price"),
        priceImportance: formData.get("priceImportance"),
        horsepower: formData.get("horsepower"),
        powerImportance: formData.get("powerImportance"),
        mileage: formData.get("mileage"),
        mileageImportance: formData.get("mileageImportance"),
        seats: formData.get("seats"),
        seatImportance: formData.get("seatImportance"),
        makes: formData.getAll("makes"),
        bodyType: formData.get("bodyType")
    }

    console.log(surveyData)

    alert("Survey submitted!")

})