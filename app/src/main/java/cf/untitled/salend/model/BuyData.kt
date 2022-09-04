package cf.untitled.salend.model

import java.util.*

data class BuyData(
    var _id: Int,
    var b_ref: String?,
    var b_item: ProductData?,
    var b_date: Date?
)

data class BuyList(
    var buyLists: ArrayList<BuyData>
)
