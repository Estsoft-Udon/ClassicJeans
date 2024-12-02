
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

// 페이지 이동 함수
function navigateToPage(pageNumber) {
    const province = document.getElementById("province").value;
    const district = document.getElementById("district").value;

    // 페이지 번호와 함께 URL에 쿼리 파라미터 추가
    let url = `/hospital_list?page=${pageNumber}&size=10`;

    // 지역 값이 존재하는 경우 URL에 포함
    if (province) {
        url += `&city=${encodeURIComponent(province)}`;
    }
    if (district) {
        url += `&district=${encodeURIComponent(district)}`;
    }

    window.location.href = url;  // 새로운 페이지로 이동
}








