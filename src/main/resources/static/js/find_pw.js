let isEmailVerified = false; // �̸��� ���� ���� ����
let isEmailChecked = false; // �����ϴ� �̸������� Ȯ��
// �̸��� �ߺ�Ȯ��
async function checkEmail() {
    const email = document.getElementById('email').value.trim();

    try {
        const response = await fetch('/api/checkEmail', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            alert('�̸��� ���� �ڵ尡 ���۵Ǿ����ϴ�.');

            fetch('/send-email', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    email: document.getElementById('email').value, // �̸��� �Է°�
                })
            })

            isEmailChecked = true;
        } else {
            alert('���Ե��� ���� �̸����Դϴ�.');

            isEmailChecked = false;
        }
    } catch (error) {
        console.error('�̸��� Ȯ�� ����:', error);
        isEmailChecked = false;
    }
}

// ���� �ڵ� ���� �Լ�
function submitAuthCode() {
    const authCode = document.getElementById('authCode').value.trim();
    const email = document.getElementById('email').value.trim();

    if (!authCode || !email) {
        alert('���� �ڵ带 �Է��ϼ���.');
        return;
    }

    // ���� �ڵ�� �̸����� ������ �����Ͽ� ���� Ȯ��
    fetch('/api/auth/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `authCode=${encodeURIComponent(authCode)}&email=${encodeURIComponent(email)}`
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // ���� ���� �� �޽��� ��ȯ
            } else {
                throw new Error('������ȣ Ȯ�� ����');
            }
        })
        .then(result => {
            alert(result); // ���� ���� �޽��� ǥ��
            isEmailVerified = true; // �̸��� ���� �Ϸ� ���·� ����
            window.location.href = '/change_pw'; // ���� �� Ư�� �������� �����̷�Ʈ
        })
        .catch(error => {
            isEmailVerified = false; // ���� ���� ���·� ����
            alert(error.message); // ���� �޽��� ǥ��
        });
}