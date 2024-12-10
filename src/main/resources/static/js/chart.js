const ctx = document.getElementById('myChart').getContext('2d');

// const labels = ['항목1', '항목2', '항목3', '항목4', '항목5', '항목6', '항목7'];
const labels = ['2024-12-01', '2024-12-02', '2024-12-03', '2024-12-04', '2024-12-05']; // 날짜를 문자열로 처리
const data1 = [30, 40, 65, 12, 20, 30, 40]; // 건강 지수 데이터(임시)
const data2 = [35, 80, 70, 50, 11, 30, 45]; // BMI 데이터(임시)

const myChart = new Chart(ctx, {
    type: 'line', // 그래프 유형 (bar, line, pie 등)
    data: {
        labels: labels,
        datasets: [
            {
                label: '건강지수',
                data: data1,
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                yAxisID: 'y1' // 첫 번째 y축을 사용
            },
            {
                label: 'BMI',
                data: data2,
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1,
                yAxisID: 'y2' // 두 번째 y축을 사용
            }
        ]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                position: 'top', // 범례 위치
            },
            title: {
                display: true,
            }
        },
        scales: {
            y1: { // 첫 번째 y축 (건강지수용)
                beginAtZero: true, // 0부터 시작
                min: 0,            // 최소값을 0으로 설정
                max: 100,          // 최대값을 100으로 설정
                ticks: {
                    stepSize: 10    // 눈금 간격을 10으로 설정
                },
                position: 'left',   // 왼쪽에 표시
                title: {
                    display: true,    // 타이틀을 표시
                    text: '건강지수',       // y2 축의 설명 텍스트
                    align: 'center',
                    rotation: 0
                }
            },
            y2: { // 두 번째 y축 (BMI용)
                beginAtZero: true,
                min: 0,
                max: 100,
                ticks: {
                    stepSize: 10
                },
                position: 'left',
                title: {
                    display: true,    // 타이틀을 표시
                    text: 'BMI',       // y2 축의 설명 텍스트
                    align: 'center',
                    rotation: 0
                },
                grid: {
                    drawOnChartArea: true // 두 번째 y축의 그리드를 숨김
                }
            },
            x: {
                type: 'category', // 'time' 대신 'category'를 사용하여 문자열을 레이블로 처리
            }
        }
    }
});