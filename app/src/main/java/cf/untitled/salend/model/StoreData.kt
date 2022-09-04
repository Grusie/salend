package cf.untitled.salend.model

data class StoreData(       //가게 정보를 담은 데이터 클래스
    var _id: String,
    var s_name: String?,
    var s_address: String?,
    var s_time: String?,
    var s_image: String?,
    var s_certified:Boolean?,
    var s_tag: List<Int>?,
    var s_lat: Double?,
    var s_lng: Double?,
)

data class StoreArray(      //가게 정보 리스트 데이터 클래스
    var stores: ArrayList<StoreData>
)
