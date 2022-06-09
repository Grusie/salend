package cf.untitled.salend.retrofit

import cf.untitled.salend.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    //GET 예제
/*    @GET("{page}")
    fun getProductArrayPage(@Path("page") page: String): Call<ProductArray>*/

    @GET("item")
    fun getTest2page() : Call<StoreItemData>

    //단일 상품
    @GET("item/{page}")
    fun getSingleProductDataPage(@Path("page") page: String): Call<ProductData>

    //상품 전체
    @GET("item/nearby/")
    fun getProductArrayPage(
        @Query("_loc") loc:String) : Call<ProductArray>

    @GET("store/")
    fun getNearbyStorePage() : Call<StoreArray>

    @GET("item/search")
    fun getProductSearchPage(@Query("query") query:String ) : Call<ProductArray2>

    @GET("store/search")
    fun getStoreSearchPage(@Query("query") query:String ) : Call<StoreArray>

    @GET("store/favorite/")
    fun getStoreFavor(
        @Query("fav") fav: String
    ) : Call<StoreArray>

//    @GET("posts/1")
//    fun getStudent(@Query("school_id") schoolId: Int,
//                   @Query("grade") grade: Int,
//                   @Query("classroom") classroom: Int): Call<ExampleResponse>
//
//
//    //POST 예제
//    @FormUrlEncoded
//    @POST("posts")
//    fun getContactsObject(@Field("idx") idx: String): Call<JsonObject>
}