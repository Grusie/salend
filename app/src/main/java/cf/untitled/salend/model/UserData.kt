package cf.untitled.salend.model

data class UserData(
    var u_id: String? = "",
    var u_name: String? = "",
    var u_store_favorite: ArrayList<String?>,
    var u_item_favorite: ArrayList<String?>,
    var u_pay_list : ArrayList<String?>
)
