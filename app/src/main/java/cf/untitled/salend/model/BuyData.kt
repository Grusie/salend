package cf.untitled.salend.model

import java.util.*

data class BuyData(     //결제 정보가 담긴 데이터 클래스
    var _id: String,
    var b_ref: String?,
    var b_item: String?,
    var b_date: Date?
)

data class BuyList(
    var buys: ArrayList<BuyData>
)
