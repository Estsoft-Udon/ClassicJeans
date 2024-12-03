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
window.addEventListener('scroll', function() {
    if (window.scrollY > 0) {
        scrTopBtn.classList.add("act");
    } else {
        scrTopBtn.classList.remove("act");
    }
});


function goToChat() {
    window.location.href = '/chat';
};

// 운세 확인 버튼
document.querySelector('.bazi_btn').addEventListener('click', function() {
    this.classList.add('dn');
    document.querySelector('.log_off_txt').classList.add('dn');
    document.querySelector('.log_on_txt').classList.remove('dn');
});

