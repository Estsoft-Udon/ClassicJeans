window.onload = function() {
    const labels = healthStatisticsList.map(item => {
        const date = new Date(item.date);
        return date.toISOString().split('T')[0];
    }).reverse();

    const data1 = healthStatisticsList.map(item => item.healthIndex).reverse();
    const data2 = healthStatisticsList.map(item => item.bmi).reverse();

    // 건강 지수의 범위 계산
    const minHealthIndex = Math.min(...data1);
    const maxHealthIndex = Math.max(...data1);

    // 건강 지수의 최소값과 최대값 설정
    const y1Min = Math.max(minHealthIndex - 10, 0);
    const y1Max = Math.min(maxHealthIndex + 10, 100);

    // BMI의 최소값과 최대값 계산
    const minBmi = Math.min(...data2);
    const maxBmi = Math.max(...data2);

    // BMI 범위 동적 설정: 평균을 기준으로 상하 2단위로 범위 설정
    const y2Min = Math.max(minBmi - 2, 0);
    const y2Max = Math.min(maxBmi + 2, 50);

    const ctx = document.getElementById('myChart').getContext('2d');

    // 차트 설정
    const myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: '건강지수',
                    data: data1,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    yAxisID: 'y1'
                },
                {
                    label: 'BMI',
                    data: data2,
                    backgroundColor: 'rgba(255, 99, 132, 0.5)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1,
                    yAxisID: 'y2'
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '건강 지수와 BMI 비교'
                }
            },
            scales: {
                y1: {
                    beginAtZero: true,
                    min: y1Min,
                    max: y1Max,
                    ticks: {
                        stepSize: 5
                    },
                    position: 'left',
                    title: {
                        display: true,
                        text: '건강지수',
                        align: 'center',
                        rotation: 0,
                        color: 'rgba(75, 192, 192, 1)'
                    }
                },
                y2: {
                    beginAtZero: true,
                    min: y2Min,
                    max: y2Max,
                    ticks: {
                        stepSize: 1
                    },
                    position: 'right',
                    title: {
                        display: true,
                        text: 'BMI',
                        align: 'center',
                        rotation: 0,
                        color: 'rgba(255, 99, 132, 1)'
                    },
                    grid: {
                        drawOnChartArea: true
                    }
                },
                x: {
                    type: 'category',
                    title: {
                        display: true,
                        text: '날짜'
                    }
                }
            }
        }
    });
};