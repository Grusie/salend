package cf.untitled.salend.interface2

import cf.untitled.salend.model.CategoryStore
import retrofit2.Call
import retrofit2.http.GET

interface IStoreService {
    @GET("store")
    fun stores() : Call<CategoryStore>
}