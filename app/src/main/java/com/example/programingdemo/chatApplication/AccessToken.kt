package com.example.programingdemo.chatApplication

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessToken {
    private const val FIREBASE_MESSAGE_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccessTokenAsync(): String? {
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"demoprojects-c7203\",\n" +
                    "  \"private_key_id\": \"2be822e9fd79432be2637415a064cb3ae0f59f89\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCb/YbX5aS/TId\\nG+1cUhi3/WqfkptfmnWaU9ALJ358luJ4/oNd0eb9ogJz8Jqhq3Ghv/3popw8NlTg\\nPNzun/cU4zE0f/CvhiY7Aqq0CGoQN5k1B/goM7oLqOnN5URCwU8zdqgymXJf8wM6\\nbouF4RRfrgXYeOPipVZaH43d/CiT34mnmg+FyRfj2ky5BU9W2EcyzziDcwkBSGBp\\ncnWnCTdIFUArAbiCJGzZHL/W3mEpimBXJ9IgcabUSsIBlNdu5FNXAN9ILbE0fnlY\\njAa80zpFysZqvoMwcrIhOpk+I/QbHEGDNDAIdHyCBWL/WDtTJLHsimUHIQro8EKj\\n790du1K/AgMBAAECggEAPKrTdvIJ2N4woz2B/6LL/KiQ+q4zVY6rsBYDnV9ay4QG\\nsOr00TK7qJrCuisyDEyinW36y0nGBtGLBRZodvopjXZf9XoP/qxURGhOG/NeKKY4\\nYwAUEXMmUxoGrYwiEtQfIrccvwD6225HLRWt6f1zLnFteIAr1p1xBpi+2qshfBWh\\noLi7S4aemJOLmREzJy+ThhbwdpgB8Y6shdo5x6kKELrhZRx9yBn6YNnrQizJGkZE\\nt+mFDOFtv68neLWrLVnpf3AoJXDEMC8+jsrCrxY2LEG19WOYUX1yK8N1weY5Ft2q\\nLaq/jloc/14ZEnViXfUSUsxTuNoFTJRe5VWWroUqQQKBgQDmcI2CzCpetXlCsz6G\\nuTvNAONGYp5O1rkpka86V97JCAaqfbaVHJ64dJMGVmb9Go+WMfl7KyThZDKIou7P\\nbCLWeJe3UtdjAO8fgqu6l7U0mi+N92PpiJsQWTpAv39jOcWlGtm+oRsKBklsKbZ5\\ndyA7vdEsjuUiO6rdjuXzds4ofwKBgQDYARr1i6tNvqYxfo79JEJccOANRp1MOKkw\\n0IEtrVzmMzm0p+r8R2w40tQzRoOhlwP3YZ1fmQVNrvrlUsqDibsmDk64ersb4YDb\\nWltxgrgDEq9hTLgELLZNvj2u+r8YKYVrfzuvx5bMuO1aeXwDjmR+S+9w9E2VjdIR\\nWdA+F9O1wQKBgQDTv2b5YpwobP8hIE37RCjERuWQ3rvzW/HHVdw7c1T/S0W80km9\\nGRBsFIqpTynRt99rNz61RD7ecdDRp05kAUy94RAgX5oVml0gilU833yd+nRqMoJ3\\nrJQ4YJVbu9vVlNed0F93gNi0zHQZVPcdBC71VoXoOOIqI8PtVQrOPkRQBwKBgFAe\\n0NYlcWwL/G89Z2JkMa1CPQkPmUbteWxe2puLw3qro4rsAhCDmZPWN9mxHSAx+Mmo\\nzovoIXx4HB41wGJcEywBgJeTBpZH5dj3QhXvi7uwEZeAsnzx7MkXeJ/P6FPZCuEd\\nt5Dy8m+LZJb7mtFi3smt9ump5KFFkpbW/pQZmHDBAoGAUhOvWLae9AC6vGgiEjaa\\n8fPGKqQs/b9sFw6jJHnf37ZSLYjOnAJGobyne/KLk64n2+Oq6tvc/dfxUX2HaKud\\n8uL5VT/tCXX6WIBAMQbvbx7wB2ti3pjyjak8AE6kGI4uxUCvzIx965Ro2zvzyfAN\\nEY/eQjREQUHBucCN1Yem4WQ=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-ujxi2@demoprojects-c7203.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"101934061439718331117\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ujxi2%40demoprojects-c7203.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(FIREBASE_MESSAGE_SCOPE))

            googleCredentials.refresh()
            return googleCredentials.accessToken.tokenValue
        } catch (e: IOException) {
            return null
        } catch (e: Exception) {
            return null
        }
    }
}
