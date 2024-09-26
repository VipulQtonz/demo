package com.example.programingdemo.chatApplication

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessToken {

    private const val FIREBASE_MESSAGE_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
    fun getAccessToken(): String? {
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"demoprojects-c7203\",\n" +
                    "  \"private_key_id\": \"ffd6284c08b139f0d1386c50a91cb22908e7d19f\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDonFvaWpSeWgRt\\ngY7jZIaUatSm+sReuPVzBLZJxfJ7Ffqm9u9Y7WzlmvnWGCwVRT9Iub4L01f++Rlr\\nMYXGYI+e/KpRJosbMHJy/Uwc2Cp9bLh6TT/rOtfriBCYPV55A3jVcCpumWGlRe1t\\nE0+h7U4cLTn4Ll8dYDfU3Dl9MpJ3JOv8ZSGycBqu0QQAhElG+WfSg9DKGU+UuMlD\\nV4dFGa8/F1uzuF7U4Cn8/rYIQmaWbZmy1Y8xp0jLj69mawDmgevXBIZZjgO7Kl1N\\n9fQDx2rqBrJcnIJsPqPuKMcx6gW2zYIl0JLCMpMRnb7Q2qAyVY8cY0cc/vgqW0GD\\nnOqjhMo/AgMBAAECggEAFW62vedDs1V6+/YIiuYrykgPoPURG+Mx5m+kNibxgSrU\\nxgaRpiF2O6+sXnAqu6j+PD61BpTqEjm0gF53Q4CJKy5/qOuCvLYZjYPOPAgk3hqi\\n4s30WkG5PF/0m3poFoFd5Fip6Dfkudw4FKlHWJWrcUgvg7EOHYBMlKXX/DOpMCX/\\nSjEC+kalprGqoNsjgzBC58JtKYx3nwHrWICCprcj251ex45KVvs2l5UGz+/IHmTN\\nMHqkLGo590BU6t3REjXsvc1TKHev9h0hzZx1ysMrNnj9k1s/L5PF8EuvyfpAnmoO\\nYLL43mj5SQv6IcQoAz7K/QIvWSryNWcqhTAyG6y0dQKBgQD1Nuw4PXxOlCbHkxMS\\n1TLeXjlAbCXxlq5HkhQl7BOoailG0zZaV31AFr7vAoMYTyK+vPahNOxjc93NS7U1\\nZc8j8jp1zbOvIUiNWGrY+e0VwrWJCupyK3d7vn6y/NEh6GWePY3OKCgVEfEgL/8G\\ntNiBx/o3a3Rg5gPYFS6di+73pQKBgQDy14UHllUzuS+uQZLbv5m4yXgeyvaEsyDF\\nH9sP0DQmIR/rwd/ygMEnYVutdcVog2lP9S2xQSjEQSZFQcm+h2AtSsRM96tBFji5\\nkIF8c7FWi+6c7488CG/t6W6uMu7vsN7bwbWR3D8zndD8lfM+c66vsDrWBZjyW7TK\\nDg8HIBJ1EwKBgHgSg8rqOKNVokOZff57zSPb6ZblHhNVYFOFcT48gke2bC3VIMlO\\nC/PE74ujpEPUBER3zSCVBKeSRerQBpR2HN8SLpQevLylxJwJzpAemXxNUo0ffjuX\\nkzNRzUKhz1oWLSQ2Kejn+vCOT48eUXecOGQcYyaeBGeqWfmGLcRRkBtNAoGBAK99\\nD6Vr/iZirRJAUOB8lZnz/dpC0eMTj5rkamoDC6yCQ48bi7qzUOIkeRvg/8eHL5OY\\nmmx9qfgpHH7wC9qj+dzBKxpz4E4owNVYmDpXYNAqT0UZ2WiIsFo9Vr6MEB8mseYd\\nFhlSEFo8scnWkvGOp65OrDX91YlOvFdH0ZRUmQIBAoGASk2xVGDK9jj2Gf4aVgdT\\nx9lwyt5XSfgs4oAL7DX3QSbaOTicibrwWrSGTIYEel+cRhVc8Z/2MCEzuRHrCrQ2\\nrP+z67QZ6W/GuFY4Cn+I940KV3ApzoDjytCIXh7XqZ7LWP9LYMn2B+mg7GAzh3Hp\\nMucCR1foukjCFryJqvic4MI=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-ujxi2@demoprojects-c7203.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"101934061439718331117\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ujxi2%40demoprojects-c7203.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"
            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))

            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(FIREBASE_MESSAGE_SCOPE))

            googleCredentials.refresh()

            return googleCredentials.accessToken.tokenValue
        } catch (e: IOException) {
            return null
        }
    }
}