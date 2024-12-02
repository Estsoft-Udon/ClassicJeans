// 대한민국 시/도와 시/군/구 데이터
const data = {
    "서울특별시": [
        "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구",
        "성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구",
        "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구",
        "서초구", "강남구", "송파구", "강동구"
    ],
    "부산광역시": [
        "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구",
        "북구", "해운대구", "사하구", "금정구", "강서구", "연제구",
        "수영구", "사상구", "기장군"
    ],
    "대구광역시": [
        "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"
    ],
    "인천광역시": [
        "중구", "동구", "미추홀구", "연수구", "남동구", "부평구",
        "계양구", "서구", "강화군", "옹진군"
    ],
    "광주광역시": [
        "동구", "서구", "남구", "북구", "광산구"
    ],
    "대전광역시": [
        "동구", "중구", "서구", "유성구", "대덕구"
    ],
    "울산광역시": [
        "중구", "남구", "동구", "북구", "울주군"
    ],
    "세종특별자치시": [
        "세종시 전체" // 세종시는 시/군/구 구분이 없음
    ],
    "경기도": [
        "수원시", "성남시", "고양시", "용인시", "부천시", "안산시", "안양시",
        "남양주시", "화성시", "평택시", "의정부시", "파주시", "시흥시",
        "김포시", "광명시", "광주시", "군포시", "하남시", "오산시", "이천시",
        "안성시", "의왕시", "양평군", "여주시", "과천시", "포천시", "양주시", "동두천시", "가평군", "연천군"
    ],
    "강원도": [
        "춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시", "삼척시",
        "홍천군", "횡성군", "영월군", "평창군", "정선군", "철원군", "화천군", "양구군", "인제군", "고성군", "양양군"
    ],
    "충청북도": [
        "청주시", "충주시", "제천시", "보은군", "옥천군", "영동군", "증평군",
        "진천군", "괴산군", "음성군", "단양군"
    ],
    "충청남도": [
        "천안시", "공주시", "보령시", "아산시", "서산시", "논산시", "계룡시",
        "당진시", "금산군", "부여군", "서천군", "청양군", "홍성군", "예산군", "태안군"
    ],
    "전라북도": [
        "전주시", "군산시", "익산시", "정읍시", "남원시", "김제시",
        "완주군", "진안군", "무주군", "장수군", "임실군", "순창군", "고창군", "부안군"
    ],
    "전라남도": [
        "목포시", "여수시", "순천시", "나주시", "광양시",
        "담양군", "곡성군", "구례군", "고흥군", "보성군", "화순군", "장흥군",
        "강진군", "해남군", "영암군", "무안군", "함평군", "영광군", "장성군",
        "완도군", "진도군", "신안군"
    ],
    "경상북도": [
        "포항시", "경주시", "김천시", "안동시", "구미시", "영주시", "영천시",
        "상주시", "문경시", "경산시", "군위군", "의성군", "청송군", "영양군",
        "영덕군", "청도군", "고령군", "성주군", "칠곡군", "예천군", "봉화군", "울진군", "울릉군"
    ],
    "경상남도": [
        "창원시", "진주시", "통영시", "사천시", "김해시", "밀양시", "거제시",
        "양산시", "의령군", "함안군", "창녕군", "고성군", "남해군", "하동군",
        "산청군", "함양군", "거창군", "합천군"
    ],
    "제주특별자치도": [
        "제주시", "서귀포시"
    ]
};

// 대한민국 시/도와 시/군/구 데이터
const provinceInput = document.getElementById("province");
const districtInput = document.getElementById("district");
const provinceList = document.getElementById("province-list");
const districtList = document.getElementById("district-list");
const searchForm = document.getElementById("region_form");

// 자동완성 리스트 생성 함수
function createAutocompleteList(input, listElement, suggestions) {
    listElement.innerHTML = ""; // 초기화
    suggestions.forEach((item) => {
        const div = document.createElement("div");
        div.textContent = item;
        div.addEventListener("click", () => {
            input.value = item;
            listElement.innerHTML = "";
            if (input.id === "province") {
                districtInput.disabled = false;
                districtInput.value = "";
                districtList.innerHTML = "";
            }
        });
        listElement.appendChild(div);
    });
}

// 시/도 입력 이벤트
provinceInput.addEventListener("input", function () {
    const query = this.value.trim();
    const suggestions = Object.keys(data).filter((province) =>
        province.startsWith(query)
    );
    createAutocompleteList(this, provinceList, suggestions);
    provinceList.style.display = suggestions.length ? "block" : "none"; // 목록 표시
});

// 시/군/구 입력 이벤트
districtInput.addEventListener("input", function () {
    const province = provinceInput.value.trim();
    if (!data[province]) return;

    const query = this.value.trim();
    const suggestions = data[province].filter((district) =>
        district.startsWith(query)
    );
    createAutocompleteList(this, districtList, suggestions);
    districtList.style.display = suggestions.length ? "block" : "none"; // 목록 표시
});

// 다른 곳 클릭 시 목록 닫기
document.addEventListener("click", function (event) {
    if (!provinceInput.contains(event.target) && !provinceList.contains(event.target)) {
        provinceList.style.display = "none";
    }
    if (!districtInput.contains(event.target) && !districtList.contains(event.target)) {
        districtList.style.display = "none";
    }
});

// 병원 검색 API 호출 함수
function searchHospitalsByRegion(province, district, page = 0, size = 10) {
    let url = `/hospital_list?`;

    if (province) {
        url += `city=${province}&`;
    }
    if (district) {
        url += `district=${district}&`;
    }

    url += `page=${page}&size=${size}`;

    // URL 끝에 불필요한 '&' 제거
    url = url.slice(0, -1);

    console.log("Generated URL:", url);

    return url;
}

// 검색 폼 제출 시 처리
searchForm.addEventListener("submit", function (event) {
    event.preventDefault();

    const province = provinceInput.value.trim();
    const district = districtInput.value.trim();

    const searchButtonClicked = event.submitter && event.submitter.type === 'submit';

    if (searchButtonClicked) {
        const url = searchHospitalsByRegion(province, district, 0, 10);  // 기본값: page=0, size=10
        window.location.href = url;
    }
});

document.addEventListener("DOMContentLoaded", function () {
    // 병원 목록을 기본적으로 모두 조회하는 코드
    const hospitalPage = window.hospitalPage || { number: 0, size: 10, totalPages: 1 }; // 서버에서 받은 페이지 정보 객체

    // 페이지네이션 텍스트 업데이트
    const pageNumbersText = document.getElementById("page-numbers");
    pageNumbersText.textContent = `Page ${hospitalPage.number + 1} of ${hospitalPage.totalPages}`;

});

// 페이지 이동 함수
function navigateToPage(pageNumber) {
    const province = document.getElementById("province").value;
    const district = document.getElementById("district").value;

    // 페이지 번호와 함께 URL에 쿼리 파라미터 추가
    let url = "/hospital_list?page=" + pageNumber + "&size=10"; // 기본 페이지네이션 URL

    // 지역 값이 존재하는 경우 URL에 포함
    if (province) {
        url += `&city=${encodeURIComponent(province)}`;
    }
    if (district) {
        url += `&district=${encodeURIComponent(district)}`;
    }

    window.location.href = url;  // 새로운 페이지로 이동
}

// 페이지네이션 업데이트 함수
function updatePagination(pageInfo) {
    const pageNumbersText = document.getElementById("page-numbers");
    pageNumbersText.textContent = `Page ${pageInfo.number + 1} of ${pageInfo.totalPages}`;

    const firstBtn = document.getElementById("first-btn");
    const prevBtn = document.getElementById("prev-btn");
    const nextBtn = document.getElementById("next-btn");
    const lastBtn = document.getElementById("last-btn");

    firstBtn.disabled = pageInfo.number === 0;
    prevBtn.disabled = pageInfo.number === 0;
    nextBtn.disabled = pageInfo.number === pageInfo.totalPages - 1;
    lastBtn.disabled = pageInfo.number === pageInfo.totalPages - 1;
}

function loadHospitals(page, size) {
    // 페이지 번호가 음수로 넘어가는 것을 방지
    if (page < 0) page = 0;

    // 기본 URL 형식
    let url = "/hospital_list?page=" + page + "&size=" + size;

    // 페이지 이동
    window.location.href = url;
}

