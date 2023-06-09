const sendBtn = document.getElementById("sendBtn");
const display = document.getElementById("display");

sendBtn.addEventListener('click', e => {
    display.textContent = "Making API request";
    fetch(
        "http://localhost:8080/sendEmail",
        {method: "GET"})
        .then(res => res.text())
        .then(res => display.textContent = res)
        .catch(err => console.log(err));
})