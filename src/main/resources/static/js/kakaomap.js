/*
    <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=43674e9fb7f4d940d2c3a46b585a4361&libraries=services"></script>
 */

// Geocoder 객체 생성
var geocoder = new kakao.maps.services.Geocoder();

// 기본 좌표 (서울 시청)
var defaultCoords = new kakao.maps.LatLng(37.5665, 126.9780);

const addressToSearch = '경기 성남시 분당구 판교역로 235 에이치스퀘어';
// 주소를 좌표로 변환
geocoder.addressSearch(addressToSearch, function (result, status) {
    var coords; // 좌표 변수 선언

    if (status === kakao.maps.services.Status.OK) {
        // 변환된 좌표 가져오기
        coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        console.log('주소 검색 성공:', coords);
    } else {
        // 주소 검색 실패 시 기본 좌표로 설정
        coords = defaultCoords;
        console.warn('주소 검색 실패. 기본 좌표를 사용합니다.');
    }

    // 지도 옵션: 좌표를 중심으로 설정
    var options = {
        center: coords, // 중심 좌표
        level: 3 // 지도 레벨 (확대 정도)
    };

    // 지도 생성
    var map = new kakao.maps.Map(document.getElementById('map'), options);

    // 마커 생성 및 지도에 추가
    var marker = new kakao.maps.Marker({
        position: coords // 마커 좌표
    });
    marker.setMap(map);
});