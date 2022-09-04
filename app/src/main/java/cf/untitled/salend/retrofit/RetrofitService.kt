package cf.untitled.salend.retrofit

import cf.untitled.salend.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {     //retrofit 통신에 사용할 함수들을 정의해둔 인터페이스

    //가게 id에 해당하는 상품들을 가져옴
    @GET("store/{id}")
    fun getStoreItem(@Path("id") id: String): Call<ProductArray2>

    //단일 상품
    @GET("item/{page}")
    fun getSingleProductDataPage(@Path("page") page: String): Call<ProductData>

    //주변 상품
    @GET("item/nearby/")
    fun getProductArrayPage(
        @Query("_loc") loc: String
    ): Call<ProductArray>

    //상품 전체를 가져옴(검색 시 사용)
    @GET("item")
    fun getProducts(): Call<ProductArray2>

    //내 주변 가게들을 가져옴
    @GET("store/")
    fun getNearbyStorePage(): Call<StoreArray>

    //가게 전체를 가져옴(검색 시 사용)
    @GET("store")
    fun getStores(): Call<StoreArray>

    //검색 시 상품 결과를 가져옴
    @GET("item/search")
    fun getProductSearchPage(@Query("query") query: String): Call<ProductArray2>

    //검색 시 가게 결과를 가져옴
    @GET("store/search")
    fun getStoreSearchPage(@Query("query") query: String): Call<StoreArray>

    //가게 찜 목록들을 가져옴
    @GET("store/favorite/")
    fun getStoreFavor(
        @Query("fav") fav: String
    ): Call<StoreArray>

    //상품 찜 목록들을 가져옴
    @GET("item/favorite")
    fun getItemFavor(
        @Query("fav") fav: String
    ): Call<ProductArray2>

    //결제 목록들을 가져옴
    @GET("buy/buylist")             //TODO buy api 생성되면 수정
    fun getBuyList(
        @Query("id") id: String
    ): Call<BuyList>

}