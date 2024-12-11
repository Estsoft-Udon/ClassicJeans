
// 병원 검색 API 호출 함수
function searchHospitalsByRegion(province, district, page = 0, size = 10) {
    let url = `/hospital-list?`;

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
    let url = `/hospital-list?page=${pageNumber}&size=10`;

    // 지역 값이 존재하는 경우 URL에 포함
    if (province) {
        url += `&city=${encodeURIComponent(province)}`;
    }
    if (district) {
        url += `&district=${encodeURIComponent(district)}`;
    }

    window.location.href = url;  // 새로운 페이지로 이동
}

document.getElementById("reservationForm").addEventListener("submit", async function (e) {
    e.preventDefault(); // 기본 제출 방지

    const formData = new FormData(e.target);
    const formObject = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/reservation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formObject),
        });

        if (response.ok) {
            alert('예약이 완료되었습니다!');
        } else {
            alert('예약에 실패했습니다. 다시 시도해주세요.');
        }
    } catch (error) {
        console.error(error);
        alert('서버와 통신 중 문제가 발생했습니다.');
    }
});

// 현재 날짜 및 시간으로 `min` 속성 설정
const now = new Date();
const year = now.getFullYear();
const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
const date = String(now.getDate()).padStart(2, '0');
const hours = String(now.getHours()).padStart(2, '0');
const minutes = String(now.getMinutes()).padStart(2, '0');

const minDateTime = `${year}-${month}-${date}T${hours}:${minutes}`;
document.getElementById("reservationDateTime").setAttribute("min", minDateTime);






