// 스크롤 인터렉션
window.addEventListener('scroll', function() {
    const header = document.querySelector("header");
    if (window.scrollY > 0) {
        header.classList.add("fixed");
    } else {
        header.classList.remove("fixed");
    }

    // 텍스트 올라오는 인터렉션
    document.querySelectorAll('.ani_load, .ani_visible').forEach(function (element) {
        var otop = element.getBoundingClientRect().top + window.scrollY;
        var wtop = window.scrollY + (window.innerHeight * 0.9);

        if (document.body.classList.contains('mo_ver')) {
            wtop = window.scrollY + (window.innerHeight * 0.9);
        }

        if (wtop > otop) {
            element.classList.add('ani_view');
        } else {
            element.classList.remove('ani_view');
        }
    });

    document.querySelectorAll('.tran_delay .tran').forEach(function (element, index) {
        var idx = (index * 1.5) / 10;
        element.style.transitionDelay = `${idx}s`;
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");
    const footer = document.querySelector("footer");

    if (scrollToTopBtn) {
        if (window.location.pathname !== "/" && window.location.pathname !== "/index") {
            window.addEventListener("scroll", function () {
                if (window.scrollY > 300) {
                    scrollToTopBtn.classList.add("show");
                } else {
                    scrollToTopBtn.classList.remove("show");
                }

                if (footer) {
                    const footerTop = footer.offsetTop;
                    const windowHeight = window.innerHeight;
                    const scrollPosition = window.scrollY + windowHeight;

                    const buttonHeight = scrollToTopBtn.offsetHeight;

                    if (scrollPosition + buttonHeight < footerTop) {
                        scrollToTopBtn.classList.add("fixed");
                        scrollToTopBtn.classList.remove("stop");
                    } else {
                        scrollToTopBtn.classList.remove("fixed");
                        scrollToTopBtn.classList.add("stop");
                    }
                }
            });

            scrollToTopBtn.addEventListener("click", function () {
                window.scrollTo({
                    top: 0,
                    behavior: "smooth"
                });
            });
        } else{
            scrollToTopBtn.style.display = "none";
        }
    } else {
        console.warn('scrollToTopBtn 요소를 찾을 수 없습니다.');
    }
});




