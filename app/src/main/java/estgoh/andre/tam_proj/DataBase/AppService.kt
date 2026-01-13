package estgoh.andre.tam_proj.DataBase

import estgoh.andre.tam_proj.DataBase.APIResponses.LoginResponse
import estgoh.andre.tam_proj.DataBase.APIResponses.OkResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.AuthProvider

interface AppService {
    @Headers("Accept: application/json")
    @POST("register")
    suspend fun addUser(
        @Body contact: User
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @POST("login")
    suspend fun loginUser(
        @Body contact: User
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("player")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<User>

    @Headers("Accept: application/json")
    @GET("quizes")
    suspend fun getAllQuizes(
        @Header("Authorization") token: String
    ): Response<List<Quiz>>

    @Headers("Accept: application/json")
    @POST("quizes")
    suspend fun addQuiz(
        @Body quiz: Quiz,
        @Header("Authorization") token: String
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @GET("quizes/{id}")
    suspend fun getQuiz(
        @Header("Authorization") token: String,
        @Path("id")quizId: Long
    ): Response<Quiz>

    @Headers("Accept: application/json")
    @PUT("quizes/{id}")
    suspend fun updateQuiz(
        @Header("Authorization") token: String,
        @Path("id")quizId: Long,
        @Body quiz: Quiz
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @DELETE("quizes/{id}")
    suspend fun deleteQuiz(
        @Header("Authorization") token: String,
        @Path("id")quizId: Long,
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @GET("questions")
    suspend fun getQuizQuestions(
        @Header("Authorization") token: String,
        @Query("quiz_id") quizId:Long
    ): Response<List<Question>>

    @Headers("Accept: application/json")
    @POST("questions")
    suspend fun addQuestion(
        @Body question: Question,
        @Header("Authorization") token: String
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @GET("questions/{id}")
    suspend fun getQuestion(
        @Header("Authorization") token: String,
        @Path("id")questionId: Long
    ): Response<Question>

    @Headers("Accept: application/json")
    @PUT("questions/{id}")
    suspend fun updateQuestion(
        @Header("Authorization") token: String,
        @Path("id")questionID: Long,
        @Body question: Question
    ): Response<OkResponse>

    @Headers("Accept: application/json")
    @DELETE("questions/{id}")
    suspend fun deleteQuestion(
        @Header("Authorization") token: String,
        @Path("id")questionId: Long,
    ): Response<OkResponse>
}