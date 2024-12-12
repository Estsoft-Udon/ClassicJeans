// 텍스트 인터랙션
window.addEventListener("load", () => {
    setTimeout(() => {
        document.querySelector(".sec01 .tit").classList.add("act");
    }, 500);
});

// 스크롤 탑 버튼
const scrollTopButton = document.querySelector(".btn_wr .scr_top_btn");

scrollTopButton.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth' // 부드러운 스크롤 효과
    });
});

window.addEventListener('scroll', () => {
    const footer = document.querySelector("footer");
    const windowHeight = window.innerHeight;
    const scrollPosition = window.scrollY + windowHeight;
    const buttonHeight = scrollTopButton.offsetHeight;

    // 스크롤 위치에 따라 버튼 표시 상태 변경
    scrollTopButton.classList.toggle("act", window.scrollY > 0);

    if (footer) {
        const footerTop = footer.offsetTop;
        const isAboveFooter = scrollPosition + buttonHeight < footerTop;
        scrollTopButton.classList.toggle("stop", !isAboveFooter);
    }
});

// 운세 확인 버튼
const baziButton = document.getElementById('baziButton');
const loadingMessage = document.getElementById('loadingMessage');
const resultContainer = document.querySelector('.cont_wr');
const logOffText = document.querySelector('.log_off_txt');
const logOnText = document.querySelector('.log_on_txt');

baziButton.addEventListener('click', () => {
    baziButton.classList.add('dn');
    logOffText.classList.add('dn');
    logOnText.classList.remove('dn');

    // 로딩 메시지 표시 및 버튼 비활성화
    loadingMessage.style.display = 'block';
    baziButton.disabled = true;

    fetch('/api/bazi')
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류: ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            if (!data?.content) {
                throw new Error("운세 데이터가 비어 있습니다.");
            }

            // 결과 표시
            resultContainer.classList.remove('dn');
            resultContainer.querySelector('pre').innerHTML = data.content;
        })
        .catch(error => {
            console.error("운세 가져오기 실패:", error);
        })
        .finally(() => {
            // 로딩 메시지 숨기기 및 버튼 활성화
            loadingMessage.style.display = 'none';
            baziButton.disabled = false;
        });
});