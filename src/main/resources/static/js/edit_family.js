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
        <div class="birth_cont">
          <div>
            <label for="family-birth-${familyIndex}"><span class="req">*</span>생년월일</label>
            <div id="ageMessage-${familyIndex}"></div>
            <input type="date" id="family-birth-${familyIndex}" name="families[${familyIndex}].birthDate" required>      
          </div>
          <div class="isLunar_box">
            <select id="family-isLunar-${familyIndex}" name="families[${familyIndex}].isLunar">
                <option value="false">양력</option>
                <option value="true">음력</option>
            </select>
          </div>
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

    // 추가된 가족 생년월일 필드에 연령 제한 추가
    const familyBirthInput = document.getElementById(`family-birth-${familyIndex - 1}`);
    const ageMessageElement = document.getElementById(`ageMessage-${familyIndex - 1}`);

    familyBirthInput.addEventListener('input', () => {
        validateAge(familyBirthInput, ageMessageElement);
    });

    // 당일 날짜까지만 표시
    const today = new Date();
    const maxDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
    familyBirthInput.max = maxDate.toISOString().split('T')[0];
});

// 연령 제한 체크 함수
function validateAge(birthInput, messageElement) {
    const dateOfBirth = birthInput.value;
    if (!dateOfBirth) {
        messageElement.textContent = '생년월일을 입력해주세요.';
        messageElement.style.color = 'red';
        return false;
    }

    const today = new Date();
    const birthDate = new Date(dateOfBirth);
    const ageLimit = 18;

    // 만 18세가 되는 날짜 계산
    const eighteenYearsLater = new Date(birthDate);
    eighteenYearsLater.setFullYear(birthDate.getFullYear() + ageLimit);

    // 만 18세가 되지 않은 날짜를 선택한 경우
    if (today < eighteenYearsLater) {
        messageElement.innerHTML = `죄송합니다.<br>만 ${ageLimit}세 이상만 추가 가능합니다.`;
        messageElement.style.color = 'red';
        return false;
    } else {
        // 만 18세 이상인 경우
        messageElement.textContent = '가입 가능합니다.';
        messageElement.style.color = 'green';
        return true;
    }
}

// form을 제출할 때, 가족 정보를 JSON 형식으로 서버로 전송
document.querySelector('form').addEventListener('submit', (event) => {
    event.preventDefault(); // 기본 폼 제출 방지

    let isValid = true;

    // 모든 가족 생년월일 입력 필드를 검사
    document.querySelectorAll('input[name^="families"][name$=".birthDate"]').forEach((birthInput) => {
        const familyId = birthInput.id.split('-')[2];
        const messageElement = document.getElementById(`ageMessage-${familyId}`);

        if (!validateAge(birthInput, messageElement)) {
            isValid = false;
        }
    });

    // 유효성 검사 실패 시 폼 제출 중단
    if (!isValid) {
        alert('입력한 정보를 확인해주세요. 가족 생년월일이 유효하지 않습니다.');
        return;
    }

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