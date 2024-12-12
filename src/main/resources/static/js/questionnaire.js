// 분석받기 버튼 스크립트
document.querySelector('.questionnaire_btn .submit_btn').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 제출 방지
    validateAllSections(); // 모든 섹션 유효성 검사
});

function validateSection(section, index) {
    const questions = section.querySelectorAll('.question');
    let sectionIsValid = true;

    questions.forEach(question => {
        const radios = question.querySelectorAll('input[type="radio"]');
        const textInputs = question.querySelectorAll('input[type="text"]');

        const isRadioChecked = Array.from(radios).some(radio => radio.checked);
        const isTextFilled = Array.from(textInputs).every(input => input.value.trim() !== '');

        if ((!isRadioChecked && radios.length > 0) || (!isTextFilled && textInputs.length > 0)) {
            sectionIsValid = false;
            question.classList.add('invalid');
        } else {
            question.classList.remove('invalid');
        }
    });

    // 프로그래스바 상태 업데이트
    const step = document.querySelectorAll('.step')[index];
    if (sectionIsValid) {
        step.classList.remove('invalid');
        step.classList.add('valid');
    } else {
        step.classList.remove('valid');
        step.classList.add('invalid');
    }

    return sectionIsValid;
}

function validateAllSections() {
    const sections = document.querySelectorAll('.step-content');
    let isValid = true;

    sections.forEach((section, index) => {
        const sectionIsValid = validateSection(section, index);
        if (!sectionIsValid) {
            isValid = false;
        }
    });

    if (isValid) {
        const loadingScreen = document.getElementById('loading-screen');
        document.getElementsByTagName('body')[0].style.overflow = 'hidden';
        loadingScreen.style.display = 'flex';
        document.querySelector('form.content').submit();
    } else {
        const firstInvalidQuestion = document.querySelector('.question.invalid');
        if (firstInvalidQuestion) {
            scrollToQuestion(firstInvalidQuestion);
            setTimeout(() => {
                const inputElements = firstInvalidQuestion.querySelectorAll('input[type="text"], input[type="radio"]');

                for (let inputElement of inputElements) {
                    if (inputElement.type === 'text' && !inputElement.value.trim()) {
                        inputElement.focus();
                        return;
                    } else if (inputElement.type === 'radio' && !inputElement.checked) {
                        inputElement.focus();
                        return;
                    }
                }
            }, 1300);
        }
        alert('해당 질문에 답변해 주세요.');
    }
}


// 라디오 버튼과 텍스트 입력에 실시간 이벤트 추가
document.querySelectorAll('input[type="radio"]').forEach(radio => {
    radio.addEventListener('click', function () {
        const section = radio.closest('.step-content');
        const index = Array.from(document.querySelectorAll('.step-content')).indexOf(section);
        validateSection(section, index);
    });
});

document.querySelectorAll('input[type="text"]').forEach(input => {
    input.addEventListener('input', function () {
        const section = input.closest('.step-content');
        const index = Array.from(document.querySelectorAll('.step-content')).indexOf(section);
        validateSection(section, index);
    });
});

document.addEventListener('scroll', handleScroll);

document.querySelectorAll('.step-button').forEach((button, index) => {
    button.addEventListener('click', () => handleStepButtonClick(index));
});

document.querySelectorAll('.step').forEach((button) => {
    button.addEventListener('click', handleStepButtonClick);
});

function handleStepButtonClick(event) {
    const steps = document.querySelectorAll('.step-content');
    const buttons = Array.from(document.querySelectorAll('.step'));

    const clickedButton = event.currentTarget;
    const index = buttons.indexOf(clickedButton);

    if (index !== -1 && steps[index]) {
        const targetSection = steps[index];
        scrollToSection(targetSection);
        activateStep(index);
    } else {
        console.error('해당 스텝을 찾을 수 없습니다.');
    }
}

function handleScroll() {
    const steps = document.querySelectorAll('.step-content');
    const progress = document.getElementById('progress');
    let activeIndex = 0;

    steps.forEach((step, index) => {
        const rect = step.getBoundingClientRect();
        if (rect.top <= window.innerHeight / 2) {
            activeIndex = index;
        }
    });

    activateStep(activeIndex);

    const progressPercentage = (activeIndex / (steps.length - 1)) * 100;
    progress.style.width = `${progressPercentage}%`;
}

function activateStep(index) {
    const progressSteps = document.querySelectorAll('.step');
    progressSteps.forEach((step) => step.classList.remove('active'));
    progressSteps[index].classList.add('active');
}

document.body.addEventListener('click', (event) => {
    if (event.target.matches('input[type="radio"]')) {
        handleRadioClick(event, event.target);
    }
});

function handleRadioClick(event, radio) {
    let currentQuestion = radio.closest('.question');

    if (!currentQuestion) {
        console.warn('라디오 버튼이 .question 내에 있지 않습니다. 상위 요소에서 재탐색 중...');
        currentQuestion = radio.closest('.step-content') || radio.parentElement;
    }

    if (!currentQuestion) {
        console.error('유효한 질문을 찾을 수 없습니다. 클릭된 라디오:', radio);
        return;
    }

    const nextQuestion = currentQuestion.nextElementSibling?.matches('.question') ? currentQuestion.nextElementSibling : null;
    const currentSection = currentQuestion.closest('.step-content');
    const nextSection = currentSection ? currentSection.nextElementSibling : null;

    radio.checked = true;

    if (!nextQuestion) {
        if (nextSection) {
            scrollToSection(nextSection);
        }
    } else {
        scrollToQuestion(nextQuestion);
    }
}

function scrollToQuestion(question) {
    const questionTop = question.getBoundingClientRect().top + window.scrollY;
    window.scrollTo({
        top: questionTop - 250,
        behavior: 'smooth'
    });
}

function scrollToSection(section) {
    const sectionTop = section.getBoundingClientRect().top + window.scrollY;
    window.scrollTo({
        top: sectionTop - 150,
        behavior: 'smooth'
    });
}
