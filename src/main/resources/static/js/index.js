// 청바지 텍스트 인터렉션
window.addEventListener("load", function () {
    setTimeout(function () {
        document.querySelector(".sec01 .tit").classList.add("act");
    }, 500);
});

// 스크롤 탑 버튼 인터렉션
const scrTopBtn = document.querySelector(".btn_wr .scr_top_btn")
scrTopBtn.addEventListener('click', function () {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'  // 부드러운 스크롤 효과
    });
});
window.addEventListener('scroll', function () {
    if (window.scrollY > 0) {
        scrTopBtn.classList.add("act");
    } else {
        scrTopBtn.classList.remove("act");
    }
});

// 운세 확인 버튼 스크립트
document.getElementById('baziButton').addEventListener('click', function () {
    const button = this;
    const loadingMessage = document.getElementById('loadingMessage');
    const resultContainer = document.querySelector('.cont_wr');

    this.classList.add('dn');
    document.querySelector('.log_off_txt').classList.add('dn');
    document.querySelector('.log_on_txt').classList.remove('dn');

    // 로딩 메시지 표시
    loadingMessage.style.display = 'block';
    button.disabled = true; // 버튼 비활성화

    fetch('/api/bazi')
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("서버 오류: " + response.statusText);
            }
        })
        .then(data => {
            if (data && data.content) {
                // 결과 표시
                resultContainer.classList.remove('dn');
                resultContainer.querySelector('pre').innerHTML = data.content;

                // 로딩 메시지 숨기기
                loadingMessage.style.display = 'none';
            } else {
                throw new Error("운세 데이터가 비어 있습니다.");
            }
        })
        .catch(error => {
            console.error(error);
            // alert("운세를 가져오는 중 문제가 발생했습니다.");
            loadingMessage.style.display = 'none'; // 로딩 메시지 숨김
        })
        .finally(() => {
            button.disabled = false; // 버튼 활성화
        });
});
