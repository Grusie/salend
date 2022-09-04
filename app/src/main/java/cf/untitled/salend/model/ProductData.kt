package cf.untitled.salend.model

data class ProductData(     //상품 정보를 담은 데이터 클래스
    var _id: String,
    var i_name: String?,
    var i_store_id: String?,
    var i_store_name: String?,
    var i_image: String?,
    var i_price: Int?,
    var i_now_price: Int?,
    var i_tag: Int?,
    var i_exp: String?
)

data class ProductArray(        //주변, 마감세일 상품 리스트 데이터 클래스
    var near_by : ArrayList<ProductData>?,
    var end_time : ArrayList<ProductData>?
)

data class ProductArray2(       //상품전체 리스트 데이터 클래스
    var items: ArrayList<ProductData>
)