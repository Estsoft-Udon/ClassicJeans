document.addEventListener('DOMContentLoaded', () => {
    const bellIcon = document.querySelector('.bell-icon');
    const notificationList = document.querySelector('.notification-list');
    const notificationCount = document.querySelector('.notification-count');

    // 종 클릭 시 알림 토글
    bellIcon.addEventListener('click', () => {
        notificationList.classList.toggle('visible');
        notificationList.classList.toggle('hidden');
    });

    // 알림 클릭 및 읽음 처리/취소
    notificationList.addEventListener('click', (event) => {
        if (event.target.classList.contains('mark-read-btn')) {
            // 읽음 처리 버튼 클릭 시
            toggleReadStatus(event.target);
        } else if (event.target.tagName === 'LI') {
            // 알림 클릭 시
            event.target.remove(); // 알림 클릭 시 삭제
            updateNotificationCount();
        }
    });

    // 외부 클릭 시 알림창 닫기
    document.addEventListener('click', (event) => {
        if (!notificationList.contains(event.target) && !bellIcon.contains(event.target)) {
            notificationList.classList.add('hidden');
            notificationList.classList.remove('visible');
        }
    });

    // 읽음 상태 토글 함수
    function toggleReadStatus(button) {
        const notificationItem = button.closest('.notification-item');
        if (notificationItem.classList.contains('unread')) {
            // 읽음 처리
            notificationItem.classList.remove('unread');
            notificationItem.classList.add('read');
            button.textContent = '읽음 취소';
        } else {
            // 읽음 취소 처리
            notificationItem.classList.remove('read');
            notificationItem.classList.add('unread');
            button.textContent = '읽음 처리';
        }

        // 알림 갯수 업데이트
        updateNotificationCount();
    }

    // 알림 갯수 업데이트
    function updateNotificationCount() {
        const remainingNotifications = notificationList.querySelectorAll('.notification-item.unread').length;
        notificationCount.textContent = remainingNotifications;

        // 알림이 없으면 카운트 숨기기
        if (remainingNotifications === 0) {
            notificationCount.style.display = 'none';
        } else {
            notificationCount.style.display = 'inline-block';
        }
    }

    // 초기 알림 갯수 설정
    updateNotificationCount();
});