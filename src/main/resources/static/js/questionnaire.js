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
        const loadingScreen = document.getElementById('loading-screen');

        document.getElementsByTagName('body')[0].style.overflow = 'hidden';
        loadingScreen.style.display = 'flex';
        document.querySelector('form.content').submit();
    } else {
        firstInvalidInput.closest('.question').scrollIntoView({ behavior: 'smooth', block: 'center' });
        firstInvalidInput.focus({ preventScroll: true }); // 포커스 시 스크롤 방지
        alert('해당 질문에 답변해 주세요.');
    }
});

document.addEventListener('scroll', handleScroll);

// 모든 라디오 버튼에 클릭 이벤트 리스너 추가
document.querySelectorAll('input[type="radio"]').forEach((radio) => {
    radio.addEventListener('click', (event) => handleRadioClick(event, radio));  // 클릭된 라디오 버튼을 전달
});

// 모든 스텝 버튼에 클릭 이벤트 리스너 추가
document.querySelectorAll('.step-button').forEach((button, index) => {
    button.addEventListener('click', () => handleStepButtonClick(index));
});

// 모든 스텝 버튼에 클릭 이벤트 리스너 추가
document.querySelectorAll('.step').forEach((button) => {
    button.addEventListener('click', handleStepButtonClick);
});

// 스텝 클릭시 해당 섹션으로 이동 스크립트
function handleStepButtonClick(event) {
    const steps = document.querySelectorAll('.step-content');
    const buttons = Array.from(document.querySelectorAll('.step'));

    // 클릭된 버튼의 인덱스를 찾음
    const clickedButton = event.currentTarget;
    const index = buttons.indexOf(clickedButton);

    // 유효한 섹션이 있는지 확인
    if (index !== -1 && steps[index]) {
        const targetSection = steps[index];
        scrollToSection(targetSection);  // 해당 섹션으로 스크롤
        activateStep(index);  // 해당 스텝을 활성화
    } else {
        console.error('해당 스텝을 찾을 수 없습니다.');
    }
}

// 프로그래스바 스크립트
function handleScroll() {
    const steps = document.querySelectorAll('.step-content');
    const progress = document.getElementById('progress');
    let activeIndex = 0;

    // 각 섹션에 대해 위치를 계산하여 활성화된 섹션을 결정
    steps.forEach((step, index) => {
        const rect = step.getBoundingClientRect();
        if (rect.top <= window.innerHeight / 2) {
            activeIndex = index;
        }
    });

    // 활성화된 단계 설정
    activateStep(activeIndex);

    // 프로그레스 바의 비율 계산 및 설정
    const progressPercentage = (activeIndex / (steps.length - 1)) * 100;
    progress.style.width = `${progressPercentage}%`;

}

function activateStep(index) {
    const progressSteps = document.querySelectorAll('.step');
    progressSteps.forEach((step) => step.classList.remove('active'));
    progressSteps[index].classList.add('active');
}

// 상위 컨테이너에 이벤트 리스너 추가 (이벤트 위임 방식)
document.body.addEventListener('click', (event) => {
    if (event.target.matches('input[type="radio"]')) {
        handleRadioClick(event, event.target);
    }
});

// 설문 버튼 관련 스크립트
function handleRadioClick(event, radio) {
    // .question 요소 찾기 (부모 탐색)
    let currentQuestion = radio.closest('.question');

    if (!currentQuestion) {
        console.warn('라디오 버튼이 .question 내에 있지 않습니다. 상위 요소에서 재탐색 중...');
        currentQuestion = radio.closest('.step-content') || radio.parentElement;
    }

    if (!currentQuestion) {
        console.error('유효한 질문을 찾을 수 없습니다. 클릭된 라디오:', radio);
        return; // 유효한 요소가 없으면 종료
    }

    // 다음 질문 및 섹션 탐색
    const nextQuestion = currentQuestion.nextElementSibling?.matches('.question') ? currentQuestion.nextElementSibling : null;
    const currentSection = currentQuestion.closest('.step-content');
    const nextSection = currentSection ? currentSection.nextElementSibling : null;

    // 라디오 버튼 체크
    radio.checked = true;

    // 다음 이동 결정
    if (!nextQuestion) {
        if (nextSection) {
            scrollToSection(nextSection);
        }
    } else {
        scrollToQuestion(nextQuestion);
    }
}

// 라디오 버튼이 속한 질문으로 스크롤
function scrollToQuestion(question) {
    const questionTop = question.offsetTop;
    window.scrollTo({
        top: questionTop - 250,  // 고정된 헤더 보정 (필요 시 조정)
        behavior: 'smooth'  // 부드러운 스크롤
    });
}

// 섹션으로 스크롤
function scrollToSection(section) {
    const sectionTop = section.offsetTop;
    window.scrollTo({
        top: sectionTop - 150,  // 고정된 헤더 보정 (필요 시 조정)
        behavior: 'smooth'  // 부드러운 스크롤
    });
}