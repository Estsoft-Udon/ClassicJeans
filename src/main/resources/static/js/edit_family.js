const familyContainer = document.getElementById('family-container');
const addFamilyButton = document.getElementById('add-family');

let familyIndex = /*[[${families.size()}]]*/ 0; // 초기 인덱스는 서버에서 전달받은 가족 수

// 가족 정보 추가 후 form을 서버로 전송
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
        <div class="isLunar_box">
            <label for="family-isLunar-${familyIndex}">음력 여부</label>
            <select id="family-isLunar-${familyIndex}" name="families[${familyIndex}].isLunar">
                <option value="false">양력</option>
                <option value="true">음력</option>
            </select>
        </div>
        <label for="family-gender-${familyIndex}"><span class="req">*</span>성별</label>
        <select id="family-gender-${familyIndex}" name="families[${familyIndex}].gender" required>
            <option value="">성별을 선택하세요</option>
            <option value="남성">남성</option>
            <option value="여성">여성</option>
        </select>
    `;

    familyContainer.appendChild(familySection);
    familyIndex++;
});

// form을 제출할 때, 가족 정보를 JSON 형식으로 서버로 전송
document.querySelector('form').addEventListener('submit', (event) => {
    event.preventDefault(); // 기본 폼 제출 방지

    const formData = new FormData(event.target);
    const familiesData = [];

    formData.forEach((value, key) => {
        const match = key.match(/families\[(\d+)\]\.(\w+)/);
        if (match) {
            const familyIndex = parseInt(match[1]);
            const field = match[2];

            if (!familiesData[familyIndex]) {
                familiesData[familyIndex] = {};
            }

            const keyMapping = {
                name: "name",
                gender: "gender",
                birthDate: "dateOfBirth",
                isLunar : "isLunar",
                relation: "relationship",
            };

            const serverKey = keyMapping[field];

            if (field === "birthDate") {
                value = new Date(value).toISOString().split('T')[0];
            }

            if (field === "gender") {
                // 성별 변환
                const genderMapping = {
                    "남성": "MALE",
                    "여성": "FEMALE",
                };
                value = genderMapping[value] || value;
            }

            if (serverKey) {
                familiesData[familyIndex][serverKey] = value;
            }
        }
    });

    fetch(`/api/family`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(familiesData),
    })
        .then(response => response.json())
        .then(data => {
            alert('가족 정보가 저장되었습니다.');
            location.href = '/mypage';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('가족 정보 저장에 실패했습니다.');
        });
});

// 가족 정보 추가 리스트 삭제 버튼 동작
function removeFamily(sectionId) {
    const isConfirmed = confirm("가족 추가 리스트를 삭제하시겠습니까?");
    if (isConfirmed) {
        // 확인을 누르면 해당 섹션을 삭제
        const familySection = document.getElementById(sectionId);
        if (familySection) {
            familySection.remove();
        }
    }
}

// 가족 정보 삭제 버튼 동작
function deleteFamily(buttonElement) {
    const familyId = buttonElement.getAttribute('data-id');
    const isConfirmed = confirm("등록된 가족 정보를 삭제하시겠습니까?");
    if (isConfirmed) {
        fetch(`/api/family/${familyId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    const familySection = document.getElementById(`family-${familyId}`);
                    if (familySection) {
                        familySection.remove();
                    }
                    alert('가족 정보가 삭제되었습니다.');
                } else {
                    alert('가족 정보 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('가족 정보 삭제에 실패했습니다.');
            });
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