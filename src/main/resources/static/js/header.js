document.addEventListener('DOMContentLoaded', () => {

    const eventSource = new EventSource(`/sse/connection`);

    eventSource.onopen = function () {
        console.log('SSE 연결 성공');
    };

    eventSource.onmessage = function (event) {
        console.log('새로운 메시지:', event.data);
    };

    const notificationList = document.querySelector(".notification-list");
    const bellIcon = document.querySelector('.bell-icon');
    const notificationCount = document.querySelector('.notification-count');

    eventSource.onmessage = function (event) {
        // 서버로부터 수신한 데이터를 파싱
        const notification = JSON.parse(event.data);

        // 새 알림을 알림 목록에 추가

        const emptyMessage = document.querySelector(".empty_alarm");
        if (emptyMessage) {
            emptyMessage.remove();
        }
        const text = formatNotificationMessage(notification);
        addNotification(notification.id, text);

        // 알림 카운트 업데이트
        updateNotificationCount();
    };

    // 종 클릭 시 알림 토글
    bellIcon.addEventListener('click', () => {
        notificationList.classList.toggle('visible');
        notificationList.classList.toggle('hidden');
    });

    // 외부 클릭 시 알림창 닫기
    document.addEventListener('click', (event) => {
        if (!notificationList.contains(event.target) && !bellIcon.contains(event.target)) {
            notificationList.classList.add('hidden');
            notificationList.classList.remove('visible');
        }
    });

    updateNotifications();

    // 읽음 상태 토글 함수
    function toggleReadStatus(id, button) {
        const notificationItem = button.closest('.notification-item');
        const url = `/api/reservation/read/${id}`;
        const isUnread = notificationItem.classList.contains('unread');
        const method = isUnread ? 'POST' : 'DELETE'; // 읽음 처리(POST) 또는 읽음 취소 처리(DELETE)

        // API 호출
        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 요청 실패');
                }
                return response.json();
            })
            .then(data => {
                // UI 업데이트
                if (isUnread) {
                    notificationItem.classList.remove('unread');
                    notificationItem.classList.add('read');
                    button.textContent = '읽음 취소';
                    console.log('읽음 취소');
                } else {
                    notificationItem.classList.remove('read');
                    notificationItem.classList.add('unread');
                    button.textContent = '읽음 처리';
                    console.log('읽음 처리');
                }

                // 알림 갯수 업데이트
                updateNotificationCount();
            })
            .catch(error => {
                console.error('알림 상태 변경 중 에러:', error);
                alert('알림 상태 변경에 실패했습니다.');
            });
    }

    // 알림 갯수 업데이트
    function updateNotificationCount() {
        const remainingNotifications = notificationList.querySelectorAll('.notification-item.unread').length;
        notificationCount.textContent = remainingNotifications;
        if (remainingNotifications === 0) {
            notificationCount.textContent = '0';
        } else {
            notificationCount.textContent = remainingNotifications;
        }
    }

    function updateNotifications() {
        // 알림 데이터를 서버에서 가져오기
        fetch("/api/notifications")
            .then(response => response.json())
            .then(data => {
                const unreadNotifications = data.filter(notification => !notification.isRead);

                if (unreadNotifications.length === 0) {
                    // 모든 알림이 읽음 상태일 때 메시지 표시
                    addEmptyMessage();
                } else {
                    // 읽지 않은 알림 데이터를 추가
                    unreadNotifications.forEach(notification => {
                        const text = formatNotificationMessage(notification);
                        addNotification(notification.id, text);
                    });
                }

                // 읽음 처리된 알림 삭제
                const readNotifications = data.filter(notification => notification.isRead);
                readNotifications.forEach(notification => {
                    fetch(`/api/reservation/${notification.id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('삭제 요청 실패');
                            }
                            console.log(`알림 ID ${notification.id} 삭제 성공`);
                        })
                        .catch(error => {
                            console.error(`알림 ID ${notification.id} 삭제 중 에러:`, error);
                        });
                });

                updateNotificationCount();
            })
            .catch(error => {
                console.error("알림 데이터를 가져오는 중 에러:", error);
            });
    }


    function addNotification(id, text) {
        console.log(text);
        const listItem = document.createElement("li");
        listItem.classList.add("notification-item");
        listItem.classList.add("unread");

        const alarmText = document.createElement("div");
        alarmText.classList.add("alarm_txt");
        alarmText.innerHTML = text;

        const markReadBtn = document.createElement("button");
        markReadBtn.classList.add("mark-read-btn");
        markReadBtn.innerHTML = "읽음 처리";

        // 읽음 처리 이벤트 추가
        markReadBtn.addEventListener("click", () => {
            toggleReadStatus(id, markReadBtn);
        });

        listItem.appendChild(alarmText);
        listItem.appendChild(markReadBtn);
        notificationList.appendChild(listItem);
    }

    function formatNotificationMessage(notification) {
        // 예약자 이름
        const reserverName = notification.reserverName;

        // 예약 시간 파싱
        const reservationTime = new Date(notification.time);
        const month = reservationTime.getMonth() + 1; // 월은 0부터 시작하므로 +1
        const day = reservationTime.getDate();
        const hour = reservationTime.getHours();
        const minute = reservationTime.getMinutes();

        // 장소 이름
        const place = notification.hospital.name;

        // 포맷된 메시지 반환
        const message = `
        ${reserverName}님, ${month}월 ${day}일 ${hour}시 ${minute}분,<br>
        ${place}에<br> 
        예약이 확정되었습니다! <br>
        잊지 말고 일정에 맞춰 방문해 주세요!😊
    `;
        return message;
    }

    function addEmptyMessage() {
        const emptyMessage = document.createElement("li");
        emptyMessage.classList.add("empty_alarm");
        emptyMessage.textContent = "받으신 알림이 없습니다.";
        notificationList.appendChild(emptyMessage);
    }

});