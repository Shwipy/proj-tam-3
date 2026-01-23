package estgoh.andre.tam_proj.DataBase

import android.content.Context.MODE_PRIVATE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofit(): Retrofit{


    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val httpClient = OkHttpClient.Builder()

    httpClient.addInterceptor(logging)

    val builder = Retrofit.Builder()
        .baseUrl("https://proj-tam-api.vercel.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())

    return builder.build()
}
