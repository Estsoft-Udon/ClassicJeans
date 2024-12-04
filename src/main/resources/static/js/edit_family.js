const familyContainer = document.getElementById('family-container');
const addFamilyButton = document.getElementById('add-family');

let familyIndex = /*[[${families.size()}]]*/ 0; // 초기 인덱스는 서버에서 전달받은 가족 수

// 가족 입력 섹션 추가
addFamilyButton.addEventListener('click', () => {
    const familySection = document.createElement('div');
    familySection.className = 'family-section';
    familySection.id = `family-${familyIndex}`;

    familySection.innerHTML = `
            <button type="button" onclick="removeFamily('${familySection.id}')" class="btn delete_btn">삭제</button>
            <label for="relationship-${familyIndex}"><span class="req">*</span>가족 관계</label>
            <input type="text" id="relationship-${familyIndex}" name="families[${familyIndex}].relation" placeholder="등록하실 가족과의 관계를 입력해주세요. 예) 부, 모, 자녀" required>
            <label for="family-name-${familyIndex}"><span class="req">*</span>이름</label> 
            <input type="text" id="family-name-${familyIndex}" name="families[${familyIndex}].name" placeholder="등록하실 가족의 이름을 입력해주세요" required>
            <label for="family-birth-${familyIndex}"><span class="req">*</span>생년월일</label>
            <input type="date" id="family-birth-${familyIndex}" name="families[${familyIndex}].birthDate" required>
            <label for="family-gender-${familyIndex}"><span class="req">*</span>성별</label>
            <input type="text" id="family-gender-${familyIndex}" name="families[${familyIndex}].gender" placeholder="등록하실 가족의 성별을 입력해주세요" required>
        `;

    familyContainer.appendChild(familySection);
    familyIndex++;
});

// 가족 정보 삭제 버튼 동작
function removeFamily(sectionId) {
    const isConfirmed = confirm("등록된 가족 정보를 삭제하시겠습니까?");
    if (isConfirmed) {
        // 확인을 누르면 해당 섹션을 삭제
        const familySection = document.getElementById(sectionId);
        if (familySection) {
            familySection.remove();
        }
    }
}

// 가족 관계 연관 검색어
const suggestions = ["부", "모", "자녀", "형제", "사촌", "조부", "조모"];
const inputField = document.getElementById("relationship");
const autocompleteList = document.getElementById("relation-autocomplete");

// 리스트 갱신
function updateAutocompleteList(query = "") {
    autocompleteList.innerHTML = ""; // 기존 리스트 초기화

    // 입력값과 연관된 검색어 필터링
    const filteredSuggestions = suggestions.filter(suggestion =>
        suggestion.includes(query)
    );

    // 필터링된 검색어를 DOM에 추가
    filteredSuggestions.forEach(suggestion => {
        const item = document.createElement("div");
        item.className = "autocomplete_item";
        item.textContent = suggestion;

        // 검색어 클릭 시 값 채우기
        item.addEventListener("click", () => {
            inputField.value = suggestion; // 선택된 값 입력 필드에 반영
            autocompleteList.classList.remove("active"); // 리스트 숨기기
        });

        autocompleteList.appendChild(item);
    });

    // 검색어 리스트 표시/숨기기
    autocompleteList.classList.toggle("active", filteredSuggestions.length > 0);
}

// 입력 필드에 포커스 시 리스트 표시
inputField.addEventListener("focus", () => {
    updateAutocompleteList(); // 초기 리스트 표시
});

// 입력 이벤트 시 리스트 갱신
inputField.addEventListener("input", function () {
    const query = this.value.trim().toLowerCase();
    updateAutocompleteList(query);
});

// 입력 필드 외부 클릭 시 리스트 숨기기
document.addEventListener("click", (e) => {
    if (!autocompleteList.contains(e.target) && e.target !== inputField) {
        autocompleteList.classList.remove("active");
    }
});