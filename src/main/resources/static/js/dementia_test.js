// 분석받기 버튼 스크립트
document.querySelector('.questionnaire_btn .submit_btn').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 제출 방지
    const sections = document.querySelectorAll('.step-content');
    let isValid = true;
    let firstInvalidQuestion = null;

    // 각 섹션의 라디오 그룹을 확인
    sections.forEach(section => {
        const questions = section.querySelectorAll('.question');
        questions.forEach(question => {
            const radios = question.querySelectorAll('input[type="radio"]');
            const isChecked = Array.from(radios).some(radio => radio.checked);

            if (!isChecked && !firstInvalidQuestion) {
                isValid = false;
                firstInvalidQuestion = question;
            }
        });
    });

    if (isValid) {
        document.querySelector('form.content').submit(); // 폼 제출
    } else {
        firstInvalidQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });
        firstInvalidQuestion.querySelector('input[type="radio"]').focus();
        alert('해당 질문에 답변해 주세요.');
    }
});