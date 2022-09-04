package cf.untitled.salend.model

import java.util.*

data class BuyData(     //결제 정보가 담긴 데이터 클래스
    var _id: Int,
    var b_ref: String?,
    var b_item: ProductData?,
    var b_date: Date?
)

data class BuyList(
    var buyLists: ArrayList<BuyData>
)
