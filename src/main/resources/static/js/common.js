document.addEventListener("DOMContentLoaded", function () {
    const header = document.querySelector("header");
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");
    const footer = document.querySelector("footer");
    const isMainPage = window.location.pathname === "/" || window.location.pathname === "/index";

    // 스크롤 이벤트 핸들러
    const handleScroll = () => {
        const scrollY = window.scrollY;

        // 헤더 고정 처리
        if (header) {
            header.classList.toggle("fixed", scrollY > 0);
        }

        // 스크롤 탑 버튼 표시/숨김 처리
        if (scrollToTopBtn && !isMainPage) {
            scrollToTopBtn.classList.toggle("show", scrollY > 300);

            if (footer) {
                const footerTop = footer.offsetTop;
                const windowHeight = window.innerHeight;
                const scrollPosition = scrollY + windowHeight;
                const buttonHeight = scrollToTopBtn.offsetHeight;

                if (scrollPosition + buttonHeight < footerTop) {
                    scrollToTopBtn.classList.add("fixed");
                    scrollToTopBtn.classList.remove("stop");
                } else {
                    scrollToTopBtn.classList.remove("fixed");
                    scrollToTopBtn.classList.add("stop");
                }
            }
        }
    };

    // 텍스트 인터랙션 처리
    const handleTextAnimation = () => {
        document.querySelectorAll(".ani_load, .ani_visible").forEach(element => {
            const elementTop = element.getBoundingClientRect().top + window.scrollY;
            const windowBottom = window.scrollY + (window.innerHeight * 0.9);
            element.classList.toggle("ani_view", windowBottom > elementTop);
        });

        document.querySelectorAll(".tran_delay .tran").forEach((element, index) => {
            element.style.transitionDelay = `${(index * 1.5) / 10}s`;
        });
    };

    // 스크롤 이벤트 등록
    window.addEventListener("scroll", () => {
        handleScroll();
        handleTextAnimation();
    });

    // 스크롤 탑 버튼 클릭 이벤트
    if (scrollToTopBtn && !isMainPage) {
        scrollToTopBtn.addEventListener("click", () => {
            window.scrollTo({ top: 0, behavior: "smooth" });
        });
    } else if (scrollToTopBtn) {
        scrollToTopBtn.style.display = "none";
    }
});