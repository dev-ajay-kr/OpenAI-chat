import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val TAG = "OpenAI"
private const val BASE_URL = "https://api.openai.com/"
private const val MODEL_NAME = "davinci"
private const val API_KEY = "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"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OpenAIService {
    @POST("v1/images/generations")
    @FormUrlEncoded
    fun generateImage(
        @Field("model") model: String = MODEL_NAME,
        @Field("prompt") prompt: String,
        @Field("num_images") numImages: Int = 1,
        @Field("size") size: String = "256x256",
        @Field("response_format") responseFormat: String = "url"
    ): Call<GenerationResponse>
}

object OpenAI {
    private val service = retrofit.create(OpenAIService::class.java)
    fun generateImage(prompt: String, callback: (String) -> Unit) {
        service.generateImage(prompt = prompt).enqueue(
            object : Callback<GenerationResponse> {
                override fun onResponse(
                    call: Call<GenerationResponse>,
                    response: Response<GenerationResponse>
                ) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.data?.get(0)?.url
                        if (imageUrl != null) {
                            callback(imageUrl)
                        } else {
                            Log.e(TAG, "Unable to generate image")
                        }
                    } else {
                        Log.e(TAG, "Error generating image: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<GenerationResponse>, t: Throwable) {
                    Log.e(TAG, "Error generating image", t)
                }
            }
        )
    }
}

data class GenerationResponse(val data: List<GenerationData>) {
    data class GenerationData(val url: String)
}
