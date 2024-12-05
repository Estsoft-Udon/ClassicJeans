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
    console.log("Selected user ID on submit: ", selectBox.value);
    if (selectBox.value === "-1") {
        event.preventDefault();
        warningMessage.textContent = "대상을 선택하세요.";
        warningMessage.style.color = 'red';
        warningMessage.style.display = "block";
    }
    // 선택된 값을 세션 스토리지에 저장
    sessionStorage.setItem("selectedUser", selectBox.value);

    // 서버로 이동 (선택된 값 포함)
    const nextUrl = `/checkout/checkout_list?selectedUser=${selectBox.value}`;
    window.location.href = nextUrl;
});

document.addEventListener("DOMContentLoaded", function() {
    checkSelection();

    const storedValue = sessionStorage.getItem("selectedUser");
    if (storedValue) {
        const selectBox = document.getElementById("user-select");
        selectBox.value = storedValue;
        checkSelection();
    }
});