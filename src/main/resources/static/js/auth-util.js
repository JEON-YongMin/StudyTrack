/**
 * Auth & Role Utility
 * 공통 인증 및 역할 확인 로직
 */
const AuthUtil = {
    getAuthInfo: () => {
        return {
            token: localStorage.getItem("accessToken"),
            userId: localStorage.getItem("userId")
        };
    },

    getMyRole: async (studyId) => {
        const { token } = AuthUtil.getAuthInfo();
        if (!token) return null;

        const authHeader = token.startsWith("Bearer ") ? token : `Bearer ${token}`;

        try {
            const response = await fetch(`/api/study/${studyId}/members/me`, {
                method: "GET",
                headers: {
                    "Authorization": authHeader
                }
            });

            if (response.ok) {
                const memberData = await response.json();
                return memberData.role;
            }
        } catch (error) {
            console.error("Role 조회 중 오류:", error);
        }
        return null;
    }
};