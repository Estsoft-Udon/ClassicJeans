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