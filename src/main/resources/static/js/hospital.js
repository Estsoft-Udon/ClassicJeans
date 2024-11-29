$(document).ready(function () {
    var currentPage = 1;
    var numOfRows = 10;
    var totalPages = 1;

    // 병원 목록을 가져오는 함수
    function fetchHospitals(page, city, district) {
        var requestData = {
            pageNo: page,
            numOfRows: numOfRows
        };

        // 필터링된 데이터가 있을 경우
        if (city && district) {
            requestData.city = city;
            requestData.district = district;
        }

        $.ajax({
            url: '/api/hospitals',
            method: 'GET',
            data: requestData,
            success: function (data) {
                var tableBody = $('#hospitalTable tbody');
                tableBody.empty(); // 기존 데이터를 지움

                // 새로운 데이터 추가
                data.forEach(function (hospital) {
                    var row = `<tr>
                        <td>${hospital.name}</td>
                        <td>${hospital.address}</td>
                        <td>${hospital.phone}</td>
                        <td>${hospital.latitude}</td>
                        <td>${hospital.longitude}</td>
                    </tr>`;
                    tableBody.append(row);
                });

                // 페이지 번호 갱신
                fetchTotalPages(city, district);
            },
            error: function () {
                alert('병원 목록을 가져오는 데 실패했습니다.');
            }
        });
    }

    // 총 페이지 수를 가져오는 함수
    function fetchTotalPages(city, district) {
        var requestData = {
            numOfRows: numOfRows
        };

        if (city && district) {
            requestData.city = city;
            requestData.district = district;
        }

        $.ajax({
            url: '/api/hospitals/pages',
            method: 'GET',
            data: requestData,
            success: function (totalPagesResponse) {
                totalPages = totalPagesResponse;
                // 페이지 번호 표시
                var pageNumbers = '';
                for (var i = 1; i <= totalPages; i++) {
                    pageNumbers += `<button class="pageButton" data-page="${i}">${i}</button> `;
                }
                $('#pageNumbers').html(pageNumbers);

                // 페이지 버튼 상태 갱신
                updatePaginationButtons();
            },
            error: function () {
                alert('총 페이지 수를 가져오는 데 실패했습니다.');
            }
        });
    }

    // 페이지 버튼 클릭 이벤트
    $(document).on('click', '.pageButton', function () {
        currentPage = $(this).data('page');
        var city = $('#city').val();
        var district = $('#district').val();
        fetchHospitals(currentPage, city, district);
    });

    // 이전 페이지 클릭
    $('#prevPage').on('click', function () {
        if (currentPage > 1) {
            currentPage--;
            var city = $('#city').val();
            var district = $('#district').val();
            fetchHospitals(currentPage, city, district);
        }
    });

    // 다음 페이지 클릭
    $('#nextPage').on('click', function () {
        if (currentPage < totalPages) {
            currentPage++;
            var city = $('#city').val();
            var district = $('#district').val();
            fetchHospitals(currentPage, city, district);
        }
    });

    // 페이지 버튼 상태 갱신
    function updatePaginationButtons() {
        // '이전' 버튼 활성화/비활성화
        if (currentPage <= 1) {
            $('#prevPage').prop('disabled', true);
        } else {
            $('#prevPage').prop('disabled', false);
        }

        // '다음' 버튼 활성화/비활성화
        if (currentPage >= totalPages) {
            $('#nextPage').prop('disabled', true);
        } else {
            $('#nextPage').prop('disabled', false);
        }
    }

    // 지역 필터링 처리
    $('#filterButton').on('click', function () {
        var city = $('#city').val();
        var district = $('#district').val();
        currentPage = 1;  // 필터링 시 첫 페이지로 리셋
        fetchHospitals(currentPage, city, district);
    });

    // 초기 데이터 로딩
    fetchHospitals(currentPage);
});