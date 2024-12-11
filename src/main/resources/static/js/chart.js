window.onload = function() {
    const labels = healthStatisticsList.map(item => {
        const date = new Date(item.date);
        return date.toISOString().split('T')[0];
    }).reverse();

    const data1 = healthStatisticsList.map(item => item.healthIndex).reverse();
    const data2 = healthStatisticsList.map(item => item.bmi).reverse();

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
                    min: 0,
                    max: 100,
                    ticks: {
                        stepSize: 10
                    },
                    position: 'left',
                    title: {
                        display: true,
                        text: '건강지수',
                        align: 'center',
                        rotation: 0
                    }
                },
                y2: {
                    beginAtZero: true,
                    min: 0,
                    max: 50,
                    ticks: {
                        stepSize: 5
                    },
                    position: 'right',
                    title: {
                        display: true,
                        text: 'BMI',
                        align: 'center',
                        rotation: 0
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
}