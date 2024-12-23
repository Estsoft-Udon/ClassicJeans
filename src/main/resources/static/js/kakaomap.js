/*
    <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=43674e9fb7f4d940d2c3a46b585a4361&libraries=services"></script>
 */

// Geocoder 객체 생성
var geocoder = new kakao.maps.services.Geocoder();

// 모달 DOM 요소 가져오기
const modal = document.getElementById("map-modal");
const closeModalButton = document.querySelector(".close");
const mapContainer = document.getElementById("map");

// 모달 열기 함수
function openMapModal() {
    modal.style.display = "flex";
    document.body.style.overflow = "hidden";
}

// 모달 닫기 함수
function closeMapModal() {
    modal.style.display = "none";
    document.body.style.overflow = "auto";
}

// 지도 렌더링 함수
function renderMap(address) {
    geocoder.addressSearch(address, function (result, status) {
        var coords;

        if (status === kakao.maps.services.Status.OK) {
            coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        } else {
            coords = new kakao.maps.LatLng(37.5665, 126.9780); // 기본 좌표
        }

        // 지도 옵션 설정
        var options = {
            center: coords,
            level: 3
        };

        // 지도 생성
        var map = new kakao.maps.Map(mapContainer, options);

        // 마커 생성 및 추가
        var marker = new kakao.maps.Marker({
            position: coords
        });
        marker.setMap(map);
    });
}

// 지도 보기 버튼 클릭 이벤트 추가
document.querySelectorAll(".view-map").forEach((button) => {
    button.addEventListener("click", (event) => {
        const address = event.target.dataset.address; // 주소 가져오기
        renderMap(address); // 지도 렌더링
        openMapModal(); // 모달 열기
    });
});

// 모달 닫기 버튼 클릭 이벤트
closeModalButton.addEventListener("click", closeMapModal);

// 모달 외부 클릭 시 닫기
modal.addEventListener("click", (event) => {
    if (event.target === modal) {
        closeMapModal();
    }
});