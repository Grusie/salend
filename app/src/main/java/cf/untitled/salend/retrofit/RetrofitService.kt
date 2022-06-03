package cf.untitled.salend.retrofit

import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    //GET 예제
/*    @GET("{page}")
    fun getProductArrayPage(@Path("page") page: String): Call<ProductArray>*/

    @GET("item/{page}")
    fun getSingleProductDataPage(@Path("page") page: String): Call<ProductData>

    //상품 전체
    @GET("item/nearby/")
    fun getProductArrayPage(
        @Query("_loc") loc:String) : Call<ProductArray>

    @GET("store/test")
    fun getNearbyStorePage() : Call<StoreArray>

//    @GET("posts/1")
//    fun getStudent(@Query("school_id") schoolId: Int,
//                   @Query("grade") grade: Int,
//                   @Query("classroom") classroom: Int): Call<ExampleResponse>
//
//
//    //POST 예제
//    @POST("posts")
//    @FormUrlEncoded
//    fun getContactsObject(@Field("idx") idx: String): Call<JsonObject>
}