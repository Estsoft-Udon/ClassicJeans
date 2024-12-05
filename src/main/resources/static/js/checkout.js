function checkSelection() {
    const selectBox = document.getElementById("user-select");
    const submitButton = document.getElementById("submit-button");
    const warningMessage = document.getElementById("warning-message");

    warningMessage.style.display = "none";

    if (selectBox.value === "-1") {
        submitButton.disabled = true;
    } else {
        submitButton.disabled = false;
    }
}

document.getElementById("submit-button").addEventListener("click", function(event) {
    const selectBox = document.getElementById("user-select");
    const warningMessage = document.getElementById("warning-message");

    if (selectBox.value === "-1") {
        event.preventDefault();
        warningMessage.textContent = "대상을 선택하세요.";
        warningMessage.style.color = 'red';
        warningMessage.style.display = "block";
    }
});

document.addEventListener("DOMContentLoaded", function() {
    checkSelection();
});