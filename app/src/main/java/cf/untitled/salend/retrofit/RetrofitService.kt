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

    @GET("store/{id}")
    fun getStoreItem(@Path("id") id: String) : Call<StoreItemData>

    //단일 상품
    @GET("item/{page}")
    fun getSingleProductDataPage(@Path("page") page: String): Call<ProductData>

    //주변 상품
    @GET("item/nearby/")
    fun getProductArrayPage(
        @Query("_loc") loc:String) : Call<ProductArray>

    @GET("item")
    fun getProducts() : Call<ProductArray2>

    @GET("store/")
    fun getNearbyStorePage() : Call<StoreArray>

    @GET("store")
    fun getStores() : Call<StoreArray>

    @GET("item/search")
    fun getProductSearchPage(@Query("query") query:String ) : Call<ProductArray2>

    @GET("store/search")
    fun getStoreSearchPage(@Query("query") query:String ) : Call<StoreArray>

    @GET("store/favorite/")
    fun getStoreFavor(
        @Query("fav") fav: String
    ) : Call<StoreArray>

    @GET("item/favorite")
    fun getItemFavor(
        @Query("fav") fav : String
    ) : Call<ProductArray2>

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