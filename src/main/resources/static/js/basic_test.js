// 분석받기 버튼 스크립트
document.querySelector('.questionnaire_btn .submit_btn').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 제출 방지
    const sections = document.querySelectorAll('.step-content');
    let isValid = true;
    let firstInvalidInput = null;

    sections.forEach(section => {
        const questions = section.querySelectorAll('.question');
        questions.forEach(question => {
            const radios = question.querySelectorAll('input[type="radio"]');
            const textInputs = question.querySelectorAll('input[type="text"]');

            const isRadioChecked = Array.from(radios).some(radio => radio.checked);
            const isTextFilled = Array.from(textInputs).every(input => input.value.trim() !== '');

            // 라디오 또는 텍스트 입력이 비어있을 경우 비어있는 요소에 포커스 설정
            if (!isRadioChecked && radios.length > 0 && !firstInvalidInput) {
                firstInvalidInput = radios[0]; // 첫 번째 라디오에 포커스
            }

            if (!isTextFilled && textInputs.length > 0 && !firstInvalidInput) {
                firstInvalidInput = Array.from(textInputs).find(input => input.value.trim() === '');
            }

            if ((!isRadioChecked && radios.length > 0) || (!isTextFilled && textInputs.length > 0)) {
                isValid = false;
            }
        });
    });

    if (isValid) {
        // 폼 데이터 수집
        const formData = new FormData(document.querySelector('form.content'));
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        // AJAX 요청
        fetch('/api/analysis/questionnaire', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.success) {
                    alert('분석이 완료되었습니다!');
                    // document.querySelector('form.content').submit();
                } else {
                    alert('분석 요청에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('오류 발생:', error);
                alert('서버와의 통신에 오류가 발생했습니다.');
            });
    } else {
        firstInvalidInput.closest('.question').scrollIntoView({ behavior: 'smooth', block: 'center' });
        firstInvalidInput.focus({ preventScroll: true });
        setTimeout(() => alert('해당 질문에 답변해 주세요.'), 300);
    }
});