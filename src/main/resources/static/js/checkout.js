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
    } else {
        // 선택된 사용자 값과 타입을 form에 설정
        const selectedUser = selectBox.value;
        const selectedOption = selectBox.options[selectBox.selectedIndex]; // 선택된 <option> 요소
        const selectedType = selectedOption.getAttribute("data-type"); // data-type 속성에서 타입을 가져옴

        // 설정된 값들을 hidden input에 넣기
        document.getElementById("selectedUser").value = selectedUser;
        document.getElementById("selectedType").value = selectedType;

        // 폼을 제출하여 서버로 전송
        document.getElementById("submit-form").submit();
    }
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