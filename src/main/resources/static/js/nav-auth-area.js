document.addEventListener("DOMContentLoaded", function() {
        const navArea = document.getElementById("navAuthArea");
        const token = localStorage.getItem("accessToken");
        const nickname = localStorage.getItem("nickname");
        const userId = localStorage.getItem("userId");
        const isExist = (val) => val && val !== "null" && val !== "undefined" && val !== "";

        if (isExist(token)) {
            const finalName = isExist(nickname) ? nickname : userId;
            // [중요] /user/mypage.html -> /user/mypage 로 수정
            navArea.innerHTML = `
                <span class="fw-bold me-3 text-dark">${finalName} 님</span>
                <a href="/user/mypage" class="btn btn-outline-primary btn-sm me-2">마이페이지</a>
                <button class="btn btn-outline-danger btn-sm" onclick="logout()">로그아웃</button>
            `;
        } else {
            navArea.innerHTML = `
                <a href="/auth/signup" class="btn btn-primary btn-sm">회원가입</a>
                <a href="/auth/login" class="btn btn-outline-primary btn-sm">로그인</a>
            `;
        }
    });
    function logout() { if(confirm("로그아웃 하시겠습니까?")) { localStorage.clear(); window.location.href = "/"; } }